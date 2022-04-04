package com.talstaya.microservice.resource.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Builder
public class ResourceResponseDTO {

    private Long id;
    private String fileName;
    private Long contentLength;
    private InputStream inputStream;
}
