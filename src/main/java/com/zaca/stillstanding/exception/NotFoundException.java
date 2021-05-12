package com.zaca.stillstanding.exception;

public class NotFoundException extends StillStandingException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -237975911570660889L;
	
	
	public NotFoundException(String type, String key) {
	    super(type + ":" + key + "未找到。", 404);

	}

}
