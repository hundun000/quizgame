package hundun.quizgame.core.model;

import java.util.List;
import java.util.Set;

import hundun.quizgame.core.model.match.BaseMatch;
import hundun.quizgame.core.model.question.Question;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class SessionDataPackage {
    String sessionId;
    List<String> questionIds;
    List<Question> dirtyQuestionIds;
    Set<String> tags;
    boolean allowImageResource = true;
    boolean allowVoiceResource = false;
    BaseMatch match;
}
