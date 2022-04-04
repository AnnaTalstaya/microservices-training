package com.talstaya.microservice.resource.service.impl;

import com.talstaya.microservice.resource.dto.ResourceResponseDTO;
import com.talstaya.microservice.resource.entity.Resource;
import com.talstaya.microservice.resource.exception.ResourceNotFoundException;
import com.talstaya.microservice.resource.repository.ResourceRepository;
import com.talstaya.microservice.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public ResourceResponseDTO save(String location) {
        Resource resource = new Resource();
        resource.setLocation(location);

        return ResourceResponseDTO.builder()
                .id(resourceRepository.save(resource).getId())
                .build();
    }

    @Override
    public String getLocationById(Long id) {
        Optional<Resource> resource = resourceRepository.findById(id);

        return resource.orElseThrow(ResourceNotFoundException::new)
                .getLocation();
    }

    public List<String> getLocationsByIds(List<Long> ids) {
        List<Resource> resources = resourceRepository.findAllById(ids);
        return resources.stream()
                .map(Resource::getLocation)
                .collect(Collectors.toList());
    }

    public void delete(List<Long> ids) {
        resourceRepository.deleteAllById(ids);
    }
}
