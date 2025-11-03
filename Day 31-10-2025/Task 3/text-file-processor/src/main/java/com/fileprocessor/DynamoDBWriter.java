package com.fileprocessor;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DynamoDBWriter {
    
    private static final Logger logger = LogManager.getLogger(DynamoDBWriter.class);
    private static final String TABLE_NAME = "FileProcessingResults";
    private final AmazonDynamoDB dynamoDBClient;

    public DynamoDBWriter() {
        this.dynamoDBClient = AmazonDynamoDBClientBuilder.standard().build();
    }

    /**
     * Writes processing result to DynamoDB
     *
     * @param result FileProcessingResult to store
     */
    public void writeResultToDynamoDB(FileProcessingResult result) {
        try {
            logger.info("Writing result to DynamoDB for file: {}", result.getFileName());
            
            // Create item attributes
            Map<String, AttributeValue> item = new HashMap<>();
            
            // Add partition key
            item.put("fileName", new AttributeValue(result.getFileName()));
            
            // Add other attributes
            item.put("lineCount", new AttributeValue().withN(String.valueOf(result.getLineCount())));
            item.put("wordCount", new AttributeValue().withN(String.valueOf(result.getWordCount())));
            item.put("charCount", new AttributeValue().withN(String.valueOf(result.getCharCount())));
            item.put("preview", new AttributeValue(result.getPreview()));
            item.put("processedDate", new AttributeValue(result.getProcessedDate()));
            
            // Create PutItem request
            PutItemRequest putItemRequest = new PutItemRequest()
                    .withTableName(TABLE_NAME)
                    .withItem(item);
            
            // Execute put operation
            PutItemResult result_response = dynamoDBClient.putItem(putItemRequest);
            
            logger.info("Successfully written to DynamoDB. Response status code: {}", 
                result_response.getSdkHttpMetadata().getHttpStatusCode());
            
        } catch (Exception e) {
            logger.error("Error writing to DynamoDB: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to write result to DynamoDB", e);
        }
    }
}
