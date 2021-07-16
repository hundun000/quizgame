package hundun.quizgame.core.model.domain;

import java.util.Arrays;
import java.util.Set;

import hundun.quizgame.core.prototype.match.AnswerType;
import hundun.quizgame.core.prototype.question.Resource;
import hundun.quizgame.core.tool.TextHelper;
import hundun.quizgame.core.view.question.QuestionView;
import lombok.Getter;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
@Getter
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
		this.answer = TextHelper.answerTextToInt(answerText);
		this.tags = tags;
		this.resource = new Resource(resourceText);
		
//		StringBuilder builder = new StringBuilder();
//		tags.forEach(e -> builder.append(e.charAt(0)));
//		builder.append("-").append(stem.substring(0, Math.min(6, stem.length())));
		this.id = String.valueOf(this.hashCode());
	}
	
	
	
	
	public boolean isCorrectOrSkipped(String answerText) {
	    if (answerText == null) {
	        return false;
	    }
		return TextHelper.answerTextToInt(answerText) == this.answer;
	}
	
	
	public String getAnswerChar() throws Exception {
	    switch (answer) {
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


    public AnswerType calculateAnswerType(String answerText) {
        if (answerText.equals(SKIP_ANSWER_TEXT)) {
            return AnswerType.SKIPPED;
        } else if (answerText.equals(TIMEOUT_ANSWER_TEXT)) {
            return AnswerType.WRONG;
        } else {
            // 正常回答A、B、C、D
            if (TextHelper.answerTextToInt(answerText) == this.answer) {
                return AnswerType.CORRECT;
            } else {
                return AnswerType.WRONG;
            }
        }
    }


    public QuestionView toQuestionDTO() {
        QuestionView dto = new QuestionView();
        dto.setId(this.id);
        dto.setAnswer(this.answer);
        dto.setStem(this.stem);
        dto.setOptions(Arrays.asList(this.options));
        dto.setResource(this.resource);
        dto.setTags(this.tags);
        return dto;
    }
    
    
}
