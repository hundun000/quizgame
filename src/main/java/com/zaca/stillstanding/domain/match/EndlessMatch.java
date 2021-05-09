package com.zaca.stillstanding.domain.match;

import com.zaca.stillstanding.domain.dto.AnswerType;
import com.zaca.stillstanding.domain.dto.MatchEvent;
import com.zaca.stillstanding.domain.dto.event.SwitchTeamEvent;
import com.zaca.stillstanding.domain.event.MatchEventFactory;
import com.zaca.stillstanding.domain.team.HealthType;
import com.zaca.stillstanding.service.BuffService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.RoleSkillService;
import com.zaca.stillstanding.service.TeamService;

/**
 * @author hundun
 * Created on 2021/04/25
 */
public class EndlessMatch extends BaseMatch {

    public EndlessMatch(QuestionService questionService, TeamService teamService, RoleSkillService roleSkillService,
            BuffService buffService, String sessionId) {
        super(
                questionService, 
                teamService, 
                roleSkillService, 
                buffService, 
                HealthType.ENDLESS,
                sessionId);
    }

    @Override
    protected int calculateCurrentHealth() {
        /*
         * 一定不死亡
         */
        return 1;
    }


    @Override
    protected SwitchTeamEvent checkSwitchTeamEvent() {
        /*
         * 一定不换队
         */
        return null;
    }

}
