package com.aakash.ragassistant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.*;

@Service
public class GroqService {

    private static final Logger logger = LoggerFactory.getLogger(GroqService.class);

    @Value("${enterprise.gateway.groq.api-key:${GROQ_API_KEY}}")
    private String groqApiKey;

    @Value("${enterprise.gateway.groq.base-url:https://api.groq.com}")
    private String baseUrl;

    @Value("${enterprise.gateway.groq.model-identifier:llama-3.3-70b-versatile}")
    private String modelIdentifier;

    @Value("${enterprise.gateway.groq.sampling-temperature:0.2}")
    private double temperature;

    @Value("${enterprise.gateway.groq.execution-token-limit:2000}")
    private int tokenLimit;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public GroqService() {
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Retry(name = "groqApi", fallbackMethod = "fallbackAnalysis")
    @CircuitBreaker(name = "groqApi", fallbackMethod = "fallbackAnalysis")
    public Map<String, Object> analyzeResume(String resumeText, String jobDescriptionText) throws Exception {

        logger.info("Starting resume analysis with Groq");

        if (groqApiKey == null || groqApiKey.isEmpty() || groqApiKey.equals("YOUR_GROQ_API_KEY_HERE")) {
            logger.error("Groq API key is missing");
            throw new Exception("GROQ_API_KEY is not configured");
        }

        String prompt = buildPrompt(resumeText, jobDescriptionText);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", modelIdentifier);
        requestBody.put("temperature", temperature);
        requestBody.put("max_tokens", tokenLimit);
        requestBody.put("stream", false);

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content",
                "You are an expert ATS Resume Analyzer AI. Return ONLY valid JSON. No markdown, no backticks."
        );

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);

        messages.add(systemMessage);
        messages.add(userMessage);
        requestBody.put("messages", messages);

        String fullUrl = baseUrl + "/openai/v1/chat/completions";
        logger.info("Calling Groq API: {}", fullUrl);

        try {
            String response = webClient.post()
                    .uri(fullUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + groqApiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(90))
                    .block();

            if (response == null) {
                throw new Exception("Null response from Groq API");
            }

            JsonNode rootNode = objectMapper.readTree(response);
            String content = rootNode
                    .path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText();

            content = cleanJson(content);
            Map<String, Object> result = objectMapper.readValue(content, Map.class);

            return ensureCompleteResult(result);

        } catch (WebClientResponseException e) {
            logger.error("HTTP Error: {}", e.getStatusCode());
            throw new Exception("API Error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Analysis error: {}", e.getMessage());
            throw e;
        }
    }

    // Fallback method when API fails
    public Map<String, Object> fallbackAnalysis(String resumeText, String jobDescriptionText, Exception e) {
        logger.warn("Using fallback analysis due to: {}", e.getMessage());

        Map<String, Object> fallback = new HashMap<>();
        fallback.put("score", 50);
        fallback.put("summary", "Analysis temporarily unavailable. Using fallback mode.");
        fallback.put("missingSkills", Arrays.asList("Unable to analyze", "Please try again later"));
        fallback.put("strengths", Arrays.asList("Resume received", "Will be analyzed when service resumes"));
        fallback.put("improvements", Arrays.asList("Service degradation", "Try again in a few minutes"));

        return fallback;
    }

    private String cleanJson(String content) {
        content = content.trim();

        if (content.startsWith("```json")) {
            content = content.substring(7);
        }
        if (content.startsWith("```")) {
            content = content.substring(3);
        }
        if (content.endsWith("```")) {
            content = content.substring(0, content.lastIndexOf("```"));
        }

        int firstBrace = content.indexOf("{");
        int lastBrace = content.lastIndexOf("}");
        if (firstBrace >= 0 && lastBrace > firstBrace) {
            content = content.substring(firstBrace, lastBrace + 1);
        }

        return content.trim();
    }

    private Map<String, Object> ensureCompleteResult(Map<String, Object> result) {
        result.putIfAbsent("score", 0);
        result.putIfAbsent("summary", "Analysis completed");
        result.putIfAbsent("missingSkills", new ArrayList<>());
        result.putIfAbsent("strengths", new ArrayList<>());
        result.putIfAbsent("improvements", new ArrayList<>());
        return result;
    }
    private String buildPrompt(String resumeText, String jobDescriptionText) {
        return String.format("""
        You are an advanced ATS Resume Analyzer AI with RAG (Retrieval-Augmented Generation) capabilities.
        
        Analyze the resume against the job description and return a comprehensive JSON response.
        
        Return ONLY valid JSON in this exact format:
        {
            "score": 75,
            "summary": "Detailed professional summary including key findings, match analysis, and recommendation (3-4 sentences)",
            "missingSkills": ["Skill 1", "Skill 2", "Skill 3", "Skill 4", "Skill 5"],
            "strengths": ["Strength 1", "Strength 2", "Strength 3", "Strength 4", "Strength 5"],
            "improvements": ["Improvement area 1", "Improvement area 2", "Improvement area 3"],
            "keywordMatch": 85,
            "experienceMatch": 70,
            "educationMatch": 90
        }
        
        INSTRUCTIONS:
        1. Score (0-100): Overall fit based on skills, experience, and keyword matching
        2. Provide 4-6 missing skills and 4-6 strengths
        3. keywordMatch: How well keywords from JD appear in resume (0-100)
        4. experienceMatch: Years and relevance of experience match (0-100)
        5. educationMatch: Educational qualification match (0-100)
        6. Be specific, objective, and provide actionable insights
        7. Consider both technical and soft skills
        
        Resume: %s
        
        Job Description: %s
        """, resumeText, jobDescriptionText);
    }
}