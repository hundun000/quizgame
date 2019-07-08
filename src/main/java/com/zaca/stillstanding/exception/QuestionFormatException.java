package com.zaca.stillstanding.exception;

import java.security.spec.ECField;

public class QuestionFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2199195735884949125L;
	
	private final int lineStart;
	private final int lineEnd;
	private final int index;
	private final String expect;
	private String fileName;
	
	public QuestionFormatException(int lineStart, int lineEnd, int index, String expect) {
		this.lineStart = lineStart;
		this.lineEnd = lineEnd;
		this.index = index;
		this.expect = expect;
		this.fileName = "";
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public String getMessage() {
		return fileName +"第"+ lineStart +"~"+ lineEnd +"行，第"+ index +"题，格式错误，预期得到的是:" + expect;
	}

}
