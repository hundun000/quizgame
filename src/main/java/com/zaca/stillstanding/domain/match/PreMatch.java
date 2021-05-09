package com.zaca.stillstanding.domain.match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaca.stillstanding.domain.dto.AnswerType;
import com.zaca.stillstanding.domain.dto.MatchEvent;
import com.zaca.stillstanding.domain.dto.event.SwitchTeamEvent;
import com.zaca.stillstanding.domain.event.MatchEventFactory;
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
            BuffService buffService, 
            String sessionId
            ) {
        super(questionService, teamService, roleSkillService, buffService,
                HealthType.SUM, sessionId
                );
    }
    
    @Override
    public void setTeamsByNames(List<String> list) throws NotFoundException {
        if (list != null && list.size() > 0) {
            // only set first team
            super.setTeamsByNames(list.subList(0, 1));
        }
    }

    
    @Override
    protected SwitchTeamEvent checkSwitchTeamEvent() {
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
