package com.halo.zhttp;

/**
 * @author Junior
 * @version 1.0
 */
public class HttpUtilsException extends Exception {

	public HttpUtilsException(String message) {
		super(message);
	}
	
	HttpUtilsException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
