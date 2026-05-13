package com.example.gedappbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageServiceAWS {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, UUID userId) {
        try {
            String key = String.format("users/%s/documents/%s_%s",
                    userId.toString(),
                    System.currentTimeMillis(),
                    file.getOriginalFilename());

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            log.info("File uploaded to S3: {}", key);
            return key;

        } catch (IOException e) {
            log.error("Failed to upload file", e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileUrl)
                    .build();

            s3Client.deleteObject(request);
            log.info("File deleted from S3: {}", fileUrl);

        } catch (Exception e) {
            log.error("Failed to delete file: {}", fileUrl, e);
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}
