package com.talstaya.microservice.resource.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RESTExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorDTO> handleResourceNotFound(ResourceNotFoundException ex)
    {
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.NOT_FOUND, ex);
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus());
    }
}
