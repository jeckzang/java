package com.example.demo.services;

public class StorageException  extends RuntimeException  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1961802149868362548L;

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
