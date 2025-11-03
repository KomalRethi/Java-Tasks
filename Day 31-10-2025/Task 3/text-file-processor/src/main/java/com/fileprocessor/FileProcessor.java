package com.fileprocessor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileProcessor {
    
    private static final Logger logger = LogManager.getLogger(FileProcessor.class);
    private static final int PREVIEW_LENGTH = 100;

    /**
     * Processes file content and extracts statistics
     *
     * @param fileName    Name of the file
     * @param fileContent Content of the file
     * @return FileProcessingResult with statistics
     */
    public FileProcessingResult processFileContent(String fileName, String fileContent) {
        try {
            logger.info("Starting file processing for: {}", fileName);
            
            // Count lines
            int lineCount = countLines(fileContent);
            
            // Count words
            int wordCount = countWords(fileContent);
            
            // Count characters (excluding newlines for accurate count)
            int charCount = fileContent.length();
            
            // Extract preview
            String preview = extractPreview(fileContent, PREVIEW_LENGTH);
            
            logger.info("File statistics - Lines: {}, Words: {}, Characters: {}", 
                lineCount, wordCount, charCount);
            
            FileProcessingResult result = new FileProcessingResult(
                fileName,
                lineCount,
                wordCount,
                charCount,
                preview
            );
            
            logger.info("File processing completed successfully");
            return result;
            
        } catch (Exception e) {
            logger.error("Error processing file content: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process file content", e);
        }
    }

    /**
     * Counts the number of lines in the content
     */
    private int countLines(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }
        return (int) content.lines().count();
    }

    /**
     * Counts the number of words in the content
     */
    private int countWords(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }
        
        // Split by whitespace and count non-empty tokens
        String[] words = content.trim().split("\\s+");
        int count = 0;
        
        for (String word : words) {
            if (!word.isEmpty()) {
                count++;
            }
        }
        
        return count;
    }

    /**
     * Extracts preview of the file content
     */
    private String extractPreview(String content, int length) {
        if (content == null) {
            return "";
        }
        
        if (content.length() <= length) {
            return content;
        }
        
        // Truncate and add ellipsis
        return content.substring(0, length) + "...";
    }
}