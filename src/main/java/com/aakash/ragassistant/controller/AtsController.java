//package com.aakash.ragassistant.controller;
//
//import com.aakash.ragassistant.dto.ResumeAnalysisResponse;
//import com.aakash.ragassistant.service.GroqService;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.text.PDFTextStripper;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import reactor.core.publisher.Mono;
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/api")
//public class AtsController {
//
//    private final GroqService groqService;
//
//    public AtsController(GroqService groqService) {
//        this.groqService = groqService;
//    }
//
//    @PostMapping("/upload-resume")
//    public Mono<ResponseEntity<ResumeAnalysisResponse>> uploadResume(
//            @RequestParam("resume") MultipartFile resume,
//            @RequestParam("jobDescription") String jobDesc) {
//
//        try {
//            // FIX: Convert file to string here before passing to service
//            String resumeText = extractText(resume);
//
//            return groqService.analyzeResume(resumeText, jobDesc)
//                    .map(ResponseEntity::ok)
//                    .defaultIfEmpty(ResponseEntity.status(503).build());
//
//        } catch (IOException e) {
//            // If PDF parsing fails, return a 400 Bad Request
//            return Mono.just(ResponseEntity.badRequest().build());
//        }
//    }
//
//    private String extractText(MultipartFile file) throws IOException {
//        // Uses Apache PDFBox to convert PDF file to String
//        try (PDDocument document = PDDocument.load(file.getBytes())) {
//            return new PDFTextStripper().getText(document);
//        }
//    }
//}