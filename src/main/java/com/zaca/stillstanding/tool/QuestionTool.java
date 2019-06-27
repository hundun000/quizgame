package com.zaca.stillstanding.tool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.yaml.snakeyaml.nodes.Tag;

import com.zaca.stillstanding.domain.question.SimpleQuestion;
import com.zaca.stillstanding.domain.question.TagManager;
import com.zaca.stillstanding.exception.SimpleQuestionFormatException;

public class QuestionTool {
	
	private static String DATA_FOLDER = "data/";
	private static String QUESTIONS_FOLDER = DATA_FOLDER + "questions/";
	
	private static List<SimpleQuestion> LoadQuestionsFromFile(File file, Set<String> tagNames) throws IOException, SimpleQuestionFormatException {
		Path path = Paths.get(file.getPath());
        List<String> lines = Files.readAllLines(path);
        return parseTextToQuestions(lines, tagNames);
	}
	
	public static List<SimpleQuestion> LoadAllQuestions() throws IOException, SimpleQuestionFormatException {
		List<SimpleQuestion> questions = new ArrayList<>();
		File mainFolder = new File(QUESTIONS_FOLDER);
		LoadQuestionsFromFolder(mainFolder, new HashSet<>(), questions);
		return questions;
	}
	
	public static List<SimpleQuestion> LoadQuestionsFromFolder(File folder, Set<String> parentTagNames, List<SimpleQuestion> questions) throws IOException, SimpleQuestionFormatException {
		File[] files = folder.listFiles();
		for (File file:files) {
			String thisTagName;
			Set<String> newParentTagNames = new HashSet<>(parentTagNames);
			
			if(file.isDirectory()) {
				thisTagName = file.getName();
				newParentTagNames.add(thisTagName);
				LoadQuestionsFromFolder(file, newParentTagNames, questions);
			} else {
				thisTagName = file.getName().substring(0, file.getName().indexOf("."));
				newParentTagNames.add(thisTagName);
				questions.addAll(LoadQuestionsFromFile(file, newParentTagNames));
			}
			TagManager.addTag(thisTagName);
		}
		return questions;
	}
	
	public static String mergeTags(String tags1, String tags2) {
		if (tags1.length() == 0) {
			return tags2;
		} else {
			return tags1 + ";" + tags2;
		}
	}
	
	/**
	 * 每一题后通过至少一个空白行分割；最后一题后可以无空行
	 * @param lines
	 * @param singleTagName
	 * @return
	 * @throws SimpleQuestionFormatException
	 */
	private static List<SimpleQuestion> parseTextToQuestions(List<String> lines, Set<String> tagNames) throws SimpleQuestionFormatException { int size = lines.size();
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
				
				SimpleQuestion question = new SimpleQuestion(stem, optionA, optionB, optionC, optionD, answer, resourceText, tagNames);
				questions.add(question);
			} catch (IndexOutOfBoundsException e) {
				throw new SimpleQuestionFormatException(i + 1, questions.size() + 1, "题目组成");
			}
			
		}
		return questions;
	}

}
