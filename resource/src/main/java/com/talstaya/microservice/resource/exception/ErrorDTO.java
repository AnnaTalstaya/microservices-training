package com.talstaya.microservice.resource.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDTO {

	private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
	private OffsetDateTime timestamp = OffsetDateTime.now();
	private String message;

	public ErrorDTO(HttpStatus status, Throwable ex)
	{
		this.status = status;
		this.message = ex.getLocalizedMessage();
	}
}