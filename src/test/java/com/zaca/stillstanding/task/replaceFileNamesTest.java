package com.zaca.stillstanding.task;

import org.junit.Test;

import com.zaca.stillstanding.tool.QuestionTool;

public class replaceFileNamesTest {
	
	@Test
	public void task() {
		System.out.println(QuestionTool.replaceFileNamesInFolder("data/questions/动画", "动画_", ""));
	}

}
