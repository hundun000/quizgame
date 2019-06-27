package com.zaca.stillstanding.domain;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.zaca.stillstanding.domain.question.SimpleQuestion;
import com.zaca.stillstanding.domain.question.TagManager;
import com.zaca.stillstanding.exception.SimpleQuestionFormatException;
import com.zaca.stillstanding.tool.QuestionTool;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
public class SimpleQuestionTest {

	@Test
	public void test() throws IOException, SimpleQuestionFormatException {
		System.out.println(QuestionTool.LoadAllQuestions().size());
		System.out.println(TagManager.getTags());
	}

}
