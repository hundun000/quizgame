package com.zaca.stillstanding.core.match.strategy;

import com.zaca.stillstanding.core.team.HealthType;
import com.zaca.stillstanding.dto.event.SwitchTeamEvent;
import com.zaca.stillstanding.service.BuffService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.RoleSkillService;
import com.zaca.stillstanding.service.TeamService;

/**
 * @author hundun
 * Created on 2021/04/25
 */
public class EndlessStrategy extends BaseMatchStrategy {

    public EndlessStrategy(QuestionService questionService, TeamService teamService, RoleSkillService roleSkillService,
            BuffService buffService) {
        super(
                questionService, 
                teamService, 
                roleSkillService, 
                buffService, 
                HealthType.ENDLESS
                );
    }

    @Override
    protected int calculateCurrentHealth() {
        /*
         * 一定不死亡
         */
        return 1;
    }


    @Override
    public SwitchTeamEvent checkSwitchTeamEvent() {
        /*
         * 一定不换队
         */
        return null;
    }

}
