package com.zaca.stillstanding.domain.question;

import java.util.Set;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
public class Question {
	
	private final String stem;
	private final String option[];
	private final int answer;
	private final Resource resource;
	private final Set<String> tags;
	
	public Question(String stem, String optionA, String optionB, String optionC, String optionD, String answerText, String resourceText, Set<String> tags) {
		this.stem = stem;
		this.option = new String[4];
		this.option[0] = optionA;
		this.option[1] = optionB;
		this.option[2] = optionC;
		this.option[3] = optionD;
		this.answer = answerTextToInt(answerText);
		this.tags = tags;
		this.resource = new Resource(resourceText);
	}
	
	
	public static int answerTextToInt(String text) {
		switch (text) {
			case "A":
			case "a":
				return 0;
			case "B":
			case "b":
				return 1;
			case "C":
			case "c":
				return 2;
			case "D":
			case "d":
				return 3;
			default:
				return -1;
		}
		
	}
	
	public boolean isCorrect(String answerText) {
		return answerTextToInt(answerText) == this.answer;
	}
	
	public Set<String> getTags() {
		return tags;
	}
	
	public String getStem() {
		return stem;
	}
}
