package com.zaca.stillstanding.domain.match;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.question.AnswerType;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.ConflictException;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.RoleSkillService;
import com.zaca.stillstanding.service.TeamService;

/**
 * @author hundun
 * Created on 2019/09/06
 */
public class PreMatch extends BaseMatch {
    

    public PreMatch(QuestionService questionService, TeamService teamService, RoleSkillService roleSkillService) {
        super(questionService, teamService, roleSkillService);
    }

    protected static final int LOSE_SUM = 5;
  
    
    /**
     * 累计答n题后死亡
     */
    @Override
    protected MatchEvent checkTeamDieEvent() {
        if (recorder.isSumAtLeastByTeam(currentTeam.getName(), LOSE_SUM)) {
            currentTeam.setAlive(false);
            return MatchEvent.getTypeTeamDie(currentTeam);
        }
        return null;
    }

    /**
     * 一定不换队
     */
    @Override
    protected MatchEvent checkSwitchTeamEvent() {
        return null;
    }

    /**
     * 固定加1分
     */
    @Override
    protected MatchEvent addScore(AnswerType answerType) {
        int addScore = 0;
        if (answerType == AnswerType.CORRECT) {
            addScore= 1;
            currentTeam.addScore(1);
        }
        return MatchEvent.getTypeAnswerResult(answerType, addScore);
    }

    

}
