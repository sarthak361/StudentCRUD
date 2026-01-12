package com.exception;

public class InsertingBenchException extends RuntimeException{
	
	public InsertingBenchException(String message) {
		super(message);
	}
	public InsertingBenchException(String message, Throwable cause) {
        super(message, cause);
    }

}
