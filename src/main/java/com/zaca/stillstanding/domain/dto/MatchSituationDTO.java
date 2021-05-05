package com.zaca.stillstanding.domain.dto;
/**
 * @author hundun
 * Created on 2021/05/08
 */

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zaca.stillstanding.domain.dto.event.AnswerResultEvent;
import com.zaca.stillstanding.domain.dto.event.FinishEvent;
import com.zaca.stillstanding.domain.dto.event.SkillResultEvent;
import com.zaca.stillstanding.domain.dto.event.StartMatchEvent;
import com.zaca.stillstanding.domain.dto.event.StubEvent;
import com.zaca.stillstanding.domain.dto.event.SwitchTeamEvent;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchSituationDTO {
    String id;
    QuestionDTO question;
    int currentTeamIndex;
    List<TeamRuntimeInfoDTO> teamRuntimeInfos;
    MatchState state;
    
    AnswerResultEvent answerResultEvent;
    SkillResultEvent skillResultEvent;
    StubEvent stubEvent;
    SwitchTeamEvent switchTeamEvent;
    FinishEvent finishEvent;
    StartMatchEvent startMatchEvent;
}
