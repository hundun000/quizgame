package com.zaca.stillstanding.core.question;

import java.util.Arrays;
import java.util.Set;

import com.zaca.stillstanding.dto.match.AnswerType;
import com.zaca.stillstanding.dto.question.QuestionDTO;
import com.zaca.stillstanding.dto.question.Resource;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
public class Question {
    public static final String SKIP_ANSWER_TEXT = "skip";
    public static final String TIMEOUT_ANSWER_TEXT = "timeout";
    
    private final String id;
	private final String stem;
	private final String[] options;
	private final int answer;
	private final Resource resource;
	private final Set<String> tags;
	
	//public static final String TIMEOUT_ANSWER_TEXT = "timeout";
	
	public Question(String stem, String optionA, String optionB, String optionC, String optionD, String answerText, String resourceText, Set<String> tags) {
		this.stem = stem;
		this.options = new String[4];
		this.options[0] = optionA;
		this.options[1] = optionB;
		this.options[2] = optionC;
		this.options[3] = optionD;
		this.answer = QuestionDTO.answerTextToInt(answerText);
		this.tags = tags;
		this.resource = new Resource(resourceText);
		
//		StringBuilder builder = new StringBuilder();
//		tags.forEach(e -> builder.append(e.charAt(0)));
//		builder.append("-").append(stem.substring(0, Math.min(6, stem.length())));
		this.id = this.toString();
	}
	
	
	
	
	public boolean isCorrectOrSkipped(String answerText) {
	    if (answerText == null) {
	        return false;
	    }
		return QuestionDTO.answerTextToInt(answerText) == this.answer;
	}
	
	public Set<String> getTags() {
		return tags;
	}
	
	public String getStem() {
		return stem;
	}
	
	public String[] getOptions() {
        return options;
    }
	
	public int getAnswer() {
        return answer;
    }
	
	public String getAnswerChar() throws Exception {
	    switch (getAnswer()) {
        case 0:
            return "A";
        case 1:
            return "B";
        case 2:
            return "C";
        case 3:
            return "D";
        default:
            throw new Exception("default int answer cannot to String.");
	    }
    }
	
	public Resource getResource() {
        return resource;
    }
	
	public String getId() {
        return id;
    }


    public AnswerType calculateAnswerType(String answerText) {
        if (answerText.equals(SKIP_ANSWER_TEXT)) {
            return AnswerType.SKIPPED;
        } else if (answerText.equals(TIMEOUT_ANSWER_TEXT)) {
            return AnswerType.WRONG;
        } else {
            // 正常回答A、B、C、D
            if (QuestionDTO.answerTextToInt(answerText) == this.answer) {
                return AnswerType.CORRECT;
            } else {
                return AnswerType.WRONG;
            }
        }
    }


    public QuestionDTO toQuestionDTO() {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(this.id);
        dto.setAnswer(this.answer);
        dto.setStem(this.stem);
        dto.setOptions(Arrays.asList(this.getOptions()));
        dto.setResource(this.resource);
        dto.setTags(this.tags);
        return dto;
    }
}
