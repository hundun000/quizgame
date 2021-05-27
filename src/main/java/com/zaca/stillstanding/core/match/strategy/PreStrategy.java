package com.zaca.stillstanding.core.match.strategy;

import com.zaca.stillstanding.core.team.HealthType;
import com.zaca.stillstanding.dto.event.SwitchTeamEvent;
import com.zaca.stillstanding.service.BuffService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.RoleSkillService;
import com.zaca.stillstanding.service.TeamService;

/**
 * @author hundun
 * Created on 2019/09/06
 */
public class PreStrategy extends BaseMatchStrategy {
    

    public PreStrategy(
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
    public SwitchTeamEvent checkSwitchTeamEvent() {
        /*
         * 一定不换队
         */
        return null;
    }



    @Override
    public int calculateCurrentHealth() {
        /*
         * 累计答n题后死亡
         */
        int fullHealth = 5;
        int currentHealth = fullHealth - parent.getRecorder().countSum(parent.getCurrentTeam().getName(), fullHealth);
        
        return currentHealth;
    }

    

}
