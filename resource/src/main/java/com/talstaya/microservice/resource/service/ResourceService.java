package com.talstaya.microservice.resource.service;

import com.talstaya.microservice.resource.dto.ResourceResponseDTO;

import java.util.List;

public interface ResourceService {

    ResourceResponseDTO save(String location);

    String getLocationById(Long id);

    List<String> getLocationsByIds(List<Long> ids);

    void delete(List<Long> ids);
}
