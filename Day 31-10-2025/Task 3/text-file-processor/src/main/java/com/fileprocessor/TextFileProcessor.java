package com.fileprocessor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextFileProcessor implements RequestHandler<S3Event, String> {
    
    private static final Logger logger = LogManager.getLogger(TextFileProcessor.class);
    private final S3FileReader s3FileReader;
    private final FileProcessor fileProcessor;
    private final DynamoDBWriter dynamoDBWriter;

    public TextFileProcessor() {
        this.s3FileReader = new S3FileReader();
        this.fileProcessor = new FileProcessor();
        this.dynamoDBWriter = new DynamoDBWriter();
    }

    @Override
    public String handleRequest(S3Event event, Context context) {
        try {
            logger.info("Received S3 event: {}", event);
            
            // Extract S3 bucket and key from event
            S3EventNotification.S3EventNotificationRecord record = event.getRecords().get(0);
            String bucket = record.getS3().getBucket().getName();
            String key = record.getS3().getObject().getKey();
            
            logger.info("Processing file - Bucket: {}, Key: {}", bucket, key);
            
            // Validate file extension
            if (!key.toLowerCase().endsWith(".txt")) {
                logger.warn("Ignoring non-text file: {}", key);
                return "File is not a .txt file";
            }
            
            // Read file from S3
            String fileContent = s3FileReader.readFileFromS3(bucket, key);
            logger.info("File content read successfully, size: {} bytes", fileContent.length());
            
            // Process file content
            FileProcessingResult result = fileProcessor.processFileContent(key, fileContent);
            logger.info("File processed - Lines: {}, Words: {}, Characters: {}", 
                result.getLineCount(), result.getWordCount(), result.getCharCount());
            
            // Store result in DynamoDB
            dynamoDBWriter.writeResultToDynamoDB(result);
            logger.info("Result stored in DynamoDB for file: {}", key);
            
            return "Successfully processed file: " + key;
            
        } catch (Exception e) {
            logger.error("Error processing S3 event: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing file", e);
        }
    }
}