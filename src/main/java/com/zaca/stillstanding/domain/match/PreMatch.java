package com.zaca.stillstanding.domain.match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.event.MatchEventFactory;
import com.zaca.stillstanding.domain.question.AnswerType;
import com.zaca.stillstanding.domain.team.HealthType;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.ConflictException;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.service.BuffService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.RoleSkillService;
import com.zaca.stillstanding.service.TeamService;

/**
 * @author hundun
 * Created on 2019/09/06
 */
public class PreMatch extends BaseMatch {
    

    public PreMatch(
            QuestionService questionService, 
            TeamService teamService, 
            RoleSkillService roleSkillService,
            BuffService buffService
            ) {
        super(questionService, teamService, roleSkillService, buffService,
                HealthType.SUM
                );
    }
    
    @Override
    public void setTeamsByNames(String... teamNames) throws NotFoundException {
        if (teamNames != null && teamNames.length > 0) {
            // only set first team
            super.setTeamsByNames(Arrays.copyOf(teamNames, 1));
        }
    }

    
    @Override
    protected MatchEvent checkSwitchTeamEvent() {
        /*
         * 一定不换队
         */
        return null;
    }



    @Override
    protected int calculateCurrentHealth() {
        /*
         * 累计答n题后死亡
         */
        int fullHealth = 5;
        int currentHealth = fullHealth - recorder.countSum(currentTeam.getName(), fullHealth);
        
        return currentHealth;
    }

    

}
