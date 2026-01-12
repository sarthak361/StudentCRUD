package com.exception;

public class DuplicateLinkException extends RuntimeException {
	public DuplicateLinkException(String message){
		super(message);
	}
	public DuplicateLinkException(String message, Throwable cause) {
        super(message, cause);
    }

}
