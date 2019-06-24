package com.zaca.stillstanding.tool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zaca.stillstanding.domain.exception.SimpleQuestionFormatException;
import com.zaca.stillstanding.domain.question.SimpleQuestion;

public class QuestionTool {
	
	private static String DATA_FOLDER = "data/";
	
	public static List<SimpleQuestion> LoadQuestionFromFile(String fileName) throws IOException, SimpleQuestionFormatException {
		Path path = Paths.get(DATA_FOLDER + fileName);
        List<String> lines = Files.readAllLines(path);
        String tagName = fileName.substring(0, fileName.indexOf("."));
        return parseTextToQuestions(lines, tagName);
	}
	
	
	private static List<SimpleQuestion> parseTextToQuestions(List<String> lines, String singleTagName) throws SimpleQuestionFormatException {
		int size = lines.size();
		String numText = lines.get(0);
		if (numText.startsWith("")) {
			numText = numText.substring(1);
		}
		int num = Integer.valueOf(numText);
		List<SimpleQuestion> questions = new ArrayList<>(num);
		
		for (int i = 2; i < size; ) {
			try {
				String stem = lines.get(i++);
				String optionA = lines.get(i++);
				String optionB = lines.get(i++);
				String optionC = lines.get(i++);
				String optionD = lines.get(i++);
				String answer = lines.get(i++);
				String resourceText = lines.get(i++);
				
				int numBlankLine;
				for (numBlankLine = 0; i + numBlankLine < size; numBlankLine++) {
					String line = lines.get(i + numBlankLine);
					if (line.length() != 0) {
						break;
					}
				}
				
				if (numBlankLine == 0 && i + numBlankLine < size) {
					throw new SimpleQuestionFormatException(i + 1, questions.size() + 1, "空行");
				}
				i += numBlankLine;
				
				SimpleQuestion question = new SimpleQuestion(stem, optionA, optionB, optionC, optionD, answer, resourceText, singleTagName);
				questions.add(question);
			} catch (IndexOutOfBoundsException e) {
				throw new SimpleQuestionFormatException(i + 1, questions.size() + 1, "题目组成");
			}
			
		}
		return questions;
	}

}
