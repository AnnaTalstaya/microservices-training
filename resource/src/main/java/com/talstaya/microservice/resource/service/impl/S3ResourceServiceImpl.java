package com.talstaya.microservice.resource.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.talstaya.microservice.resource.dto.ResourceResponseDTO;
import com.talstaya.microservice.resource.exception.ResourceNotFoundException;
import com.talstaya.microservice.resource.service.S3ResourceService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Log4j2
public class S3ResourceServiceImpl implements S3ResourceService {

    private static final String FILE_EXTENSION = "fileExtension";

    private AmazonS3 amazonS3;
    private String bucketName;

    public S3ResourceServiceImpl(AmazonS3 amazonS3, @Value("${aws.s3.bucket-name}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;

        initializeBucket();
    }

    @Override
    public String uploadMP3(MultipartFile mp3) {
        String key = RandomStringUtils.randomAlphanumeric(50);

        try {
            amazonS3.putObject(bucketName, key, mp3.getInputStream(), extractObjectMetadata(mp3));
        } catch (IOException e) {
            log.error("Error while write object to S3", e);
        }
        return key;
    }

    @Override
    public ResourceResponseDTO download(String location) {
        try {
            S3Object s3Object = amazonS3.getObject(bucketName, location);

            String filename = location + "." + s3Object.getObjectMetadata().getUserMetadata().get(FILE_EXTENSION);
            Long contentLength = s3Object.getObjectMetadata().getContentLength();

            return ResourceResponseDTO.builder()
                    .fileName(filename)
                    .contentLength(contentLength)
                    .inputStream(s3Object.getObjectContent())
                    .build();

        } catch (RuntimeException e) {
            throw new ResourceNotFoundException();
        }

    }

    @Override
    @Transactional
    public void deleteByLocations(List<String> locations) {
        locations.forEach(key -> amazonS3.deleteObject(bucketName, key));
    }

    private void initializeBucket() {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket(bucketName);
        }
    }

    private ObjectMetadata extractObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        objectMetadata.getUserMetadata().put(FILE_EXTENSION, FilenameUtils.getExtension(file.getOriginalFilename()));

        return objectMetadata;
    }
}
