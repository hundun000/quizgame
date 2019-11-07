package com.zaca.stillstanding.exception;

public class ConflictException extends StillStandingException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4076058696749505391L;
	
	private final String type;
	private final String key;
	
	public ConflictException(String type, String key) {
		super(type + ":" + key + "已存在。", 1);
	    this.type = type;
		this.key = key;
	}

}
