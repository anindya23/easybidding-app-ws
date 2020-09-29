package com.easybidding.app.ws.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {
	
	private static final long serialVersionUID = -148172270194192332L;

    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

	public ResourceNotFoundException(String message) {
		super(message);
	}
	
    public ResourceNotFoundException(final Throwable cause) {
        super(cause);
    }

}