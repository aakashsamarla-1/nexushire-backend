package com.aakash.ragassistant.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class PdfService {

    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);

    public String extractText(MultipartFile file) throws Exception {
        logger.info("Extracting text from PDF: {}", file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            if (text == null || text.trim().isEmpty()) {
                throw new Exception("No text extracted from PDF");
            }

            logger.info("Extracted {} characters from PDF", text.length());
            return text;
        }
    }

    public String extractTextFromDocx(MultipartFile file) throws Exception {
        logger.info("Extracting text from DOCX: {}", file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream();
             XWPFDocument document = new XWPFDocument(inputStream)) {

            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            String text = extractor.getText();
            extractor.close();

            if (text == null || text.trim().isEmpty()) {
                throw new Exception("No text extracted from DOCX");
            }

            logger.info("Extracted {} characters from DOCX", text.length());
            return text;
        }
    }

    public String extractTextFromDoc(MultipartFile file) throws Exception {
        logger.info("Extracting text from DOC: {}", file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream();
             HWPFDocument document = new HWPFDocument(inputStream)) {

            WordExtractor extractor = new WordExtractor(document);
            String text = extractor.getText();
            extractor.close();

            if (text == null || text.trim().isEmpty()) {
                throw new Exception("No text extracted from DOC");
            }

            logger.info("Extracted {} characters from DOC", text.length());
            return text;
        }
    }
}