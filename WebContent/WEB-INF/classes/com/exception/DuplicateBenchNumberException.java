package com.exception;

public class DuplicateBenchNumberException extends RuntimeException {
	
	public DuplicateBenchNumberException(String message) {
		super(message);
	}
	public DuplicateBenchNumberException(String message, Throwable cause) {
        super(message, cause);
    }

}
