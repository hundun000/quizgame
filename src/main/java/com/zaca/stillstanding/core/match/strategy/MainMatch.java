package com.zaca.stillstanding.core.match.strategy;

import com.zaca.stillstanding.core.event.MatchEventFactory;
import com.zaca.stillstanding.core.team.HealthType;
import com.zaca.stillstanding.core.team.Team;
import com.zaca.stillstanding.dto.event.SwitchTeamEvent;
import com.zaca.stillstanding.service.BuffService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.RoleSkillService;
import com.zaca.stillstanding.service.TeamService;

/**
 * @author hundun
 * Created on 2019/09/10
 */
public class MainMatch extends BaseMatchStrategy {
    


    public MainMatch(
            QuestionService questionService, 
            TeamService teamService, 
            RoleSkillService roleSkillService,
            BuffService buffService,
            String sessionId
            ) {
        super(questionService, teamService, roleSkillService, buffService,
                HealthType.CONSECUTIVE_WRONG_AT_LEAST
                );
    }

    
    @Override
    public SwitchTeamEvent checkSwitchTeamEvent() {
        /*
         * 每一题换队（被调用一定换）
         */
        Team lastTeam = parent.getCurrentTeam();
        switchToNextTeam();
        return MatchEventFactory.getTypeSwitchTeam(lastTeam, parent.getCurrentTeam());
    }


    @Override
    protected int calculateCurrentHealth() {
        int fullHealth = 1;
        /*
         * 连续答错数, 即为健康度的减少量。
         */
        int currentHealth = fullHealth - parent.getRecorder().countConsecutiveWrong(parent.getCurrentTeam().getName(), fullHealth);
        return currentHealth;
    }

}