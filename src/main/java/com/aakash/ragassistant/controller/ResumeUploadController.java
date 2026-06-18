package com.aakash.ragassistant.controller;

import com.aakash.ragassistant.service.GroqService;
import com.aakash.ragassistant.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ResumeUploadController {

    private static final Logger logger = LoggerFactory.getLogger(ResumeUploadController.class);

    private final GroqService groqService;
    private final PdfService pdfService;

    @PostMapping("/upload-resume")
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile resumeFile,
            @RequestParam("jobDescriptionFile") MultipartFile jobDescriptionFile
    ) {
        logger.info("=== Resume Analysis Request ===");
        logger.info("Resume: {} ({} bytes)", resumeFile.getOriginalFilename(), resumeFile.getSize());
        logger.info("JD: {} ({} bytes)", jobDescriptionFile.getOriginalFilename(), jobDescriptionFile.getSize());

        try {
            // Validate files
            if (resumeFile.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Resume file is empty"));
            }
            if (jobDescriptionFile.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Job description file is empty"));
            }

            // Extract text
            logger.info("Extracting text from files...");
            String resumeText = extractTextFromFile(resumeFile);
            String jobDescriptionText = extractTextFromFile(jobDescriptionFile);

            if (resumeText == null || resumeText.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Could not extract text from resume"));
            }
            if (jobDescriptionText == null || jobDescriptionText.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Could not extract text from job description"));
            }

            logger.info("Resume text length: {} chars", resumeText.length());
            logger.info("JD text length: {} chars", jobDescriptionText.length());

            // Analyze
            logger.info("Calling Groq for analysis...");
            Map<String, Object> result = groqService.analyzeResume(resumeText, jobDescriptionText);

            logger.info("Analysis complete! Score: {}", result.get("score"));
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("Analysis failed: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "RAG Assistant"));
    }

    private String extractTextFromFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        if (fileName == null) throw new RuntimeException("Invalid file");

        String lowerName = fileName.toLowerCase();

        if (lowerName.endsWith(".pdf")) {
            return pdfService.extractText(file);
        } else if (lowerName.endsWith(".docx")) {
            return pdfService.extractTextFromDocx(file);
        } else if (lowerName.endsWith(".doc")) {
            return pdfService.extractTextFromDoc(file);
        } else if (lowerName.endsWith(".txt")) {
            return new String(file.getBytes());
        } else {
            throw new RuntimeException("Unsupported file type: " + fileName);
        }
    }
}