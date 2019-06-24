package com.zaca.stillstanding.domain.question;

import com.zaca.stillstanding.domain.tag.Tag;
import com.zaca.stillstanding.domain.tag.TagFactory;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
public class SimpleQuestion {
	
	private final String stem;
	private final String option[];
	private final int answer;
	private final Resource resource;
	private final Tag tags[];
	
	public SimpleQuestion(String stem, String optionA, String optionB, String optionC, String optionD, String answerText, String resourceText, String singleTagName) {
		this.stem = stem;
		this.option = new String[4];
		this.option[0] = optionA;
		this.option[1] = optionB;
		this.option[2] = optionC;
		this.option[3] = optionD;
		this.answer = answerTextToInt(answerText);
		this.tags = new Tag[1];
		this.tags[0] = TagFactory.getTag(singleTagName);
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
}
