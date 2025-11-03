package com.fileprocessor;

import com.google.gson.annotations.SerializedName;
import java.time.Instant;

public class FileProcessingResult {
    
    @SerializedName("fileName")
    private String fileName;
    
    @SerializedName("lineCount")
    private int lineCount;
    
    @SerializedName("wordCount")
    private int wordCount;
    
    @SerializedName("charCount")
    private int charCount;
    
    @SerializedName("preview")
    private String preview;
    
    @SerializedName("processedDate")
    private String processedDate;

    // Default constructor for DynamoDB mapping
    public FileProcessingResult() {
        this.processedDate = Instant.now().toString();
    }

    // Constructor with parameters
    public FileProcessingResult(String fileName, int lineCount, int wordCount, 
                               int charCount, String preview) {
        this.fileName = fileName;
        this.lineCount = lineCount;
        this.wordCount = wordCount;
        this.charCount = charCount;
        this.preview = preview;
        this.processedDate = Instant.now().toString();
    }

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getCharCount() {
        return charCount;
    }

    public void setCharCount(int charCount) {
        this.charCount = charCount;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(String processedDate) {
        this.processedDate = processedDate;
    }

    @Override
    public String toString() {
        return "FileProcessingResult{" +
                "fileName='" + fileName + '\'' +
                ", lineCount=" + lineCount +
                ", wordCount=" + wordCount +
                ", charCount=" + charCount +
                ", preview='" + preview + '\'' +
                ", processedDate='" + processedDate + '\'' +
                '}';
    }
}
