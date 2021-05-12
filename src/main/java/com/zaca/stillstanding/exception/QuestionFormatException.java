package com.zaca.stillstanding.exception;

public class QuestionFormatException extends StillStandingException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2199195735884949125L;
	
	public QuestionFormatException(int lineStart, int lineEnd, int index, String expect) {
		super("第"+ lineStart +"~"+ lineEnd +"行，第"+ index +"题，格式错误，预期得到的是:" + expect, 3);
	}
	

	public QuestionFormatException(QuestionFormatException e, String fileName) {
	    super(fileName + e.getMessage(), e.getRetcode());
	}

}
