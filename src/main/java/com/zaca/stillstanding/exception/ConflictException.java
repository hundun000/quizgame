package com.zaca.stillstanding.exception;

public class ConflictException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4076058696749505391L;
	
	private final String type;
	private final String key;
	
	public ConflictException(String type, String key) {
		this.type = type;
		this.key = key;
	}
	
	@Override
	public String getMessage() {
		return type + ":" + key + "已存在。";
	}

}
