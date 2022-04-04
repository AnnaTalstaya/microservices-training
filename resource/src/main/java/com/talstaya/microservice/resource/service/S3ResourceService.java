package com.talstaya.microservice.resource.service;

import com.talstaya.microservice.resource.dto.ResourceResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3ResourceService {

    String uploadMP3(MultipartFile mp3);

    ResourceResponseDTO download(String location);

    void deleteByLocations(List<String> locations);
}
