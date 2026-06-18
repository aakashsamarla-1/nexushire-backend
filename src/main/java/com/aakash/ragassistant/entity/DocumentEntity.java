//package com.aakash.ragassistant.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Entity
//@Table(name = "documents")
//@Getter
//@Setter
//public class DocumentEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(columnDefinition = "TEXT")
//    private String content;
//
//    private String fileName;
//
//    private String documentType;
//
//    @Column(columnDefinition = "vector(384)")
//    private float[] embedding;
//}