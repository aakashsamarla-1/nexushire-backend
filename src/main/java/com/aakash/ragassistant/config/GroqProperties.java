//package com.aakash.ragassistant.config;
//
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//
//@Data
//@Configuration
//@ConfigurationProperties(prefix = "enterprise.gateway.groq")
//public class GroqProperties {
//    private String apiKey;
//    private String baseUrl;
//    private String modelEndpoint;
//    private String modelIdentifier;
//    private double samplingTemperature;
//    private int executionTokenLimit;
//
//    // Dynamic asset pointers
//    private Resource activePromptLayout;
//    private Resource targetSchemaLayout;
//
//    // Parameter map keys (Removes String Hardcoding)
//    private String keyModel = "model";
//    private String keyTemperature = "temperature";
//    private String keyMaxTokens = "max_tokens";
//    private String keyMessages = "messages";
//    private String keyRole = "role";
//    private String keyContent = "content";
//    private String roleUserValue = "user";
//
//    // Application categorization labels
//    private String docTypeResume = "RESUME";
//    private String docTypeJobDesc = "JOB_DESCRIPTION";
//    private String identityJobDescFile = "job-description";
//}