package com.aakash.ragassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RagAssistantApplication {
    public static void main(String[] args) {
        SpringApplication.run(RagAssistantApplication.class, args);
        System.out.println("========================================");
        System.out.println("🚀 Resume Intelligence API Started!");
        System.out.println("📍 Listening on: http://localhost:8080");
        System.out.println("📝 API Endpoint: POST /api/upload-resume");
        System.out.println("========================================");
    }
}