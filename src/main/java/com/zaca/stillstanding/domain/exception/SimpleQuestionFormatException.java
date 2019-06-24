package com.zaca.stillstanding.domain.exception;

import java.security.spec.ECField;

public class SimpleQuestionFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2199195735884949125L;
	
	private final int line;
	private final int index;
	private final String expect;
	
	public SimpleQuestionFormatException(int line, int index, String expect) {
		this.line = line;
		this.index = index;
		this.expect = expect;
	}
	
	@Override
	public String getMessage() {
		return "第" + line + "行，第" + index + "题，格式错误，预期得到的是:" + expect;
	}

}
