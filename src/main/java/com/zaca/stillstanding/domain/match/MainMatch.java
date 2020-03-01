package com.zaca.stillstanding.domain.match;

import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.question.AnswerType;
import com.zaca.stillstanding.domain.team.HealthType;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.TeamDeadException;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.RoleSkillService;
import com.zaca.stillstanding.service.TeamService;

/**
 * @author hundun
 * Created on 2019/09/10
 */
public class MainMatch extends BaseMatch {

    public MainMatch(QuestionService questionService, TeamService teamService, RoleSkillService roleSkillService) {
        super(questionService, teamService, roleSkillService);
    }

    
    @Override
    protected MatchEvent addScoreAndCountHealth(AnswerType answerType) {
        /*
         * 固定加1
         */
        int addScore = 0;
        if (answerType == AnswerType.CORRECT) {
            addScore= 1;
            currentTeam.addScore(1);
        }
        
        /*
         * 连续答错数, 即为健康度的减少量。
         */
        int fullHealth = 1;
        int currentHealth = fullHealth - recorder.countConsecutiveWrong(currentTeam.getName(), fullHealth);
        
        if (currentHealth == 0) {
            currentTeam.setAlive(false);
        }
        
        return MatchEvent.getTypeAnswerResult(answerType, addScore, currentTeam.getMatchScore(), HealthType.CONSECUTIVE_WRONG_AT_LEAST, currentHealth);
    }

    
    @Override
    protected MatchEvent checkSwitchTeamEvent() {
        /*
         * 每一题换队（被调用一定换）
         */
        Team lastTeam = currentTeam;
        switchToNextTeam();
        return MatchEvent.getTypeSwitchTeam(lastTeam, currentTeam);
    }

}
