package com.zaca.stillstanding.domain.match;

import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.question.AnswerType;
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
    protected MatchEvent addScore(AnswerType answerType) {
        /*
         * 固定加1
         */
        int addScore = 0;
        if (answerType == AnswerType.CORRECT) {
            addScore= 1;
            currentTeam.addScore(1);
        }
        return MatchEvent.getTypeAnswerResult(answerType, addScore, currentTeam.getMatchScore());
    }

    
    @Override
    protected MatchEvent checkTeamDieEvent() {
        /*
         * 连续答错1题则死亡
         */
        if (recorder.isConsecutiveWrongAtLeastByTeam(currentTeam.getName(), 1)) {
            currentTeam.setAlive(false);
            return MatchEvent.getTypeTeamDie(currentTeam);
        }
        return null;
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
