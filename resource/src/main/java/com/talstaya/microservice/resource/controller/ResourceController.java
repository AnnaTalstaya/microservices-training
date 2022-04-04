package com.talstaya.microservice.resource.controller;

import com.talstaya.microservice.resource.dto.ResourceResponseDTO;
import com.talstaya.microservice.resource.service.ResourceService;
import com.talstaya.microservice.resource.service.S3ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private S3ResourceService s3ResourceService;

    @Autowired
    private ResourceService resourceService;

    @PostMapping
    @Transactional
    public ResponseEntity<ResourceResponseDTO> uploadMP3(@RequestParam("mp3") MultipartFile mp3) {
        String location = s3ResourceService.uploadMP3(mp3);
        ResourceResponseDTO resource = resourceService.save(location);

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<org.springframework.core.io.Resource> download(@PathVariable("id") Long resourceId) {
        String location = resourceService.getLocationById(resourceId);
        ResourceResponseDTO resourceDTO = s3ResourceService.download(location);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resourceDTO.getFileName())
                .contentLength(resourceDTO.getContentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(resourceDTO.getInputStream()));
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<HttpStatus> delete(@RequestParam(name = "id", required = false) Long[] ids) {
        List<Long> resourceIds = Arrays.asList(ids);

        List<String> resourcePaths = resourceService.getLocationsByIds(resourceIds);
        s3ResourceService.deleteByLocations(resourcePaths);
        resourceService.delete(resourceIds);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
