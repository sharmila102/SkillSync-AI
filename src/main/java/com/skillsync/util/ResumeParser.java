package com.skillsync.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class ResumeParser {

    public String parse(InputStream inputStream, String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty");
        }

        String lowerCaseName = fileName.toLowerCase();
        if (lowerCaseName.endsWith(".pdf")) {
            return parsePdf(inputStream);
        } else if (lowerCaseName.endsWith(".docx")) {
            return parseDocx(inputStream);
        } else if (lowerCaseName.endsWith(".txt")) {
            return parseTxt(inputStream);
        } else {
            throw new IllegalArgumentException("Unsupported file type. Please upload PDF or DOCX.");
        }
    }

    private String parsePdf(InputStream inputStream) throws IOException {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String parseDocx(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            try (XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
                return extractor.getText();
            }
        }
    }

    private String parseTxt(InputStream inputStream) throws IOException {
        byte[] bytes = inputStream.readAllBytes();
        return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    }
}
