package com.aakash.ragassistant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

@Service
public class EmbeddingService {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddingService.class);
    private static final int EMBEDDING_DIMENSION = 384;

    public EmbeddingService() {
        logger.info("EmbeddingService initialized with dimension: {}", EMBEDDING_DIMENSION);
    }

    /**
     * Generate deterministic embedding from text using SHA-256
     * This is a fallback method - for production, use OpenAI/Cohere APIs
     */
    public float[] generateEmbedding(String text) {
        if (text == null || text.isEmpty()) {
            logger.warn("Empty text provided for embedding generation");
            return new float[EMBEDDING_DIMENSION];
        }

        try {
            // Use SHA-256 for consistent, deterministic embeddings
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));

            float[] embedding = new float[EMBEDDING_DIMENSION];

            // Distribute hash bytes across embedding dimensions
            for (int i = 0; i < EMBEDDING_DIMENSION; i++) {
                int hashIndex = i % hash.length;
                embedding[i] = (float) (hash[hashIndex] & 0xFF) / 255.0f;
            }

            // Normalize the embedding
            return normalize(embedding);

        } catch (Exception e) {
            logger.error("Error generating embedding: {}", e.getMessage());
            return new float[EMBEDDING_DIMENSION];
        }
    }

    /**
     * Calculate cosine similarity between two embeddings
     */
    public double cosineSimilarity(float[] embedding1, float[] embedding2) {
        if (embedding1.length != embedding2.length) {
            logger.warn("Embedding dimensions don't match: {} vs {}", embedding1.length, embedding2.length);
            return 0.0;
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < embedding1.length; i++) {
            dotProduct += embedding1[i] * embedding2[i];
            norm1 += embedding1[i] * embedding1[i];
            norm2 += embedding2[i] * embedding2[i];
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private float[] normalize(float[] embedding) {
        double norm = 0.0;
        for (float v : embedding) {
            norm += v * v;
        }
        norm = Math.sqrt(norm);

        if (norm > 0) {
            for (int i = 0; i < embedding.length; i++) {
                embedding[i] /= norm;
            }
        }

        return embedding;
    }
}