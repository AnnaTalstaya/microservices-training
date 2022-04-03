package com.talstaya.microservices.processor.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Mp3DTO {

    private String name;
    private String artist;
    private String album;
    private String length;
    private String year;

}
