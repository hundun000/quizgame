package hundun.quizgame.core.dto.event;

import hundun.quizgame.core.dto.match.AnswerType;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class AnswerResultEvent extends MatchEvent {
    AnswerType result;
    int addScore;
    String addScoreTeamName;
}
