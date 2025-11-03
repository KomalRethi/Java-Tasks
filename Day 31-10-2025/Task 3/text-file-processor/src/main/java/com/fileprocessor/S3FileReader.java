package com.fileprocessor;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class S3FileReader {
    
    private static final Logger logger = LogManager.getLogger(S3FileReader.class);
    private final AmazonS3 s3Client;

    public S3FileReader() {
        this.s3Client = AmazonS3ClientBuilder.standard().build();
    }

    /**
     * Reads a text file from S3 and returns its content as a String
     *
     * @param bucketName S3 bucket name
     * @param objectKey   S3 object key (file path)
     * @return File content as String
     */
    public String readFileFromS3(String bucketName, String objectKey) {
        try {
            logger.info("Reading file from S3 - Bucket: {}, Key: {}", bucketName, objectKey);
            
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectKey);
            S3Object s3Object = s3Client.getObject(getObjectRequest);
            
            // Read the S3 object content
            String fileContent = new BufferedReader(
                    new InputStreamReader(s3Object.getObjectContent(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            
            logger.info("Successfully read file from S3. Size: {} characters", fileContent.length());
            return fileContent;
            
        } catch (Exception e) {
            logger.error("Error reading file from S3: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to read file from S3", e);
        }
    }
}
