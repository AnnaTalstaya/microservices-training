package com.talstaya.microservices.processor.service;

import com.talstaya.microservices.processor.dto.Mp3DTO;
import org.springframework.web.multipart.MultipartFile;

public interface ResourceProcessorService {

    Mp3DTO parsingMp3(MultipartFile mp3);
}
