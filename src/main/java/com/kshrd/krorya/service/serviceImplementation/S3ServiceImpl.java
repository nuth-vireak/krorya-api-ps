package com.kshrd.krorya.service.serviceImplementation;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.kshrd.krorya.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3ServiceImpl(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    @Override
    public String uploadFile(String keyName, MultipartFile file) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getBytes());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        String newFileName = generateUUIDFileName(keyName);
        PutObjectResult putObjectResult = s3client.putObject(bucketName, newFileName, inputStream, metadata);
        log.info("Uploaded file metadata: {}", putObjectResult);
        return newFileName;
    }

    public S3Object getFile(String keyName) {
        log.info("URL FOR IMAGE: {}", s3client.getObject(bucketName, keyName).getObjectContent());
        return s3client.getObject(bucketName, keyName);
    }

    @Override
    public void deleteFile(String keyName) {
        try {
            s3client.deleteObject(bucketName, keyName);
            log.info("Successfully deleted file with key: {}", keyName);
        } catch (AmazonS3Exception e) {
            log.error("Error occurred while deleting file with key: {}", keyName, e);
        }
    }

    @Override
    public String getFileUrl(String fileName) {
        return "https://aws-kshrd-final-project.s3.eu-central-1.amazonaws.com/" + fileName;
    }

    private String generateUUIDFileName(String fileName) {
        String uuid = UUID.randomUUID().toString();
        String fileExtension = getFileExtension(fileName);
        return uuid + "." + fileExtension;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public static interface TagServiceImpl {
    }
}
