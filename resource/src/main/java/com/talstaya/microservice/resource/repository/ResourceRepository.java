package com.talstaya.microservice.resource.repository;

import com.talstaya.microservice.resource.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

}
