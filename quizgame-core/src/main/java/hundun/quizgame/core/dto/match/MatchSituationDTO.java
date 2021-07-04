package hundun.quizgame.core.dto.match;
/**
 * @author hundun
 * Created on 2021/05/08
 */

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hundun.quizgame.core.dto.event.AnswerResultEvent;
import hundun.quizgame.core.dto.event.FinishEvent;
import hundun.quizgame.core.dto.event.SkillResultEvent;
import hundun.quizgame.core.dto.event.StartMatchEvent;
import hundun.quizgame.core.dto.event.SwitchQuestionEvent;
import hundun.quizgame.core.dto.event.SwitchTeamEvent;
import hundun.quizgame.core.dto.question.QuestionDTO;
import hundun.quizgame.core.dto.team.TeamRuntimeInfoDTO;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchSituationDTO {
    String id;
    QuestionDTO question;
    TeamRuntimeInfoDTO currentTeamRuntimeInfo;
    int currentTeamIndex;
    List<TeamRuntimeInfoDTO> teamRuntimeInfos;
    MatchState state;
    Set<ClientActionType> actionAdvices;
    
    AnswerResultEvent answerResultEvent;
    SkillResultEvent skillResultEvent;
    SwitchQuestionEvent switchQuestionEvent;
    SwitchTeamEvent switchTeamEvent;
    FinishEvent finishEvent;
    StartMatchEvent startMatchEvent;
}
