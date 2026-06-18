//package com.aakash.ragassistant.service;
//
//import com.aakash.ragassistant.entity.DocumentEntity;
//import com.aakash.ragassistant.repository.DocumentRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class RagService {
//
//    private final DocumentRepository repository;
//    private final EmbeddingService embeddingService;
//
//    public void saveDocument(String content,
//                             String fileName,
//                             String type) {
//
//        float[] embedding = embeddingService.generateEmbedding(content);
//
//        DocumentEntity entity = new DocumentEntity();
//
//        entity.setContent(content);
//        entity.setFileName(fileName);
//        entity.setDocumentType(type);
//        entity.setEmbedding(embedding);
//
//        repository.save(entity);
//    }
//}