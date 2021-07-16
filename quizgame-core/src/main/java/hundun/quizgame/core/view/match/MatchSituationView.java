package hundun.quizgame.core.view.match;
/**
 * @author hundun
 * Created on 2021/05/08
 */

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hundun.quizgame.core.prototype.event.AnswerResultEvent;
import hundun.quizgame.core.prototype.event.FinishEvent;
import hundun.quizgame.core.prototype.event.SkillResultEvent;
import hundun.quizgame.core.prototype.event.StartMatchEvent;
import hundun.quizgame.core.prototype.event.SwitchQuestionEvent;
import hundun.quizgame.core.prototype.event.SwitchTeamEvent;
import hundun.quizgame.core.prototype.match.ClientActionType;
import hundun.quizgame.core.prototype.match.MatchState;
import hundun.quizgame.core.view.question.QuestionView;
import hundun.quizgame.core.view.team.TeamRuntimeView;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchSituationView {
    String id;
    QuestionView question;
    TeamRuntimeView currentTeamRuntimeInfo;
    int currentTeamIndex;
    List<TeamRuntimeView> teamRuntimeInfos;
    MatchState state;
    Set<ClientActionType> actionAdvices;
    
    AnswerResultEvent answerResultEvent;
    SkillResultEvent skillResultEvent;
    SwitchQuestionEvent switchQuestionEvent;
    SwitchTeamEvent switchTeamEvent;
    FinishEvent finishEvent;
    StartMatchEvent startMatchEvent;
}
