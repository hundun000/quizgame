package com.zaca.stillstanding.core.match.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaca.stillstanding.dto.match.MatchStrategyType;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.service.BuffService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.RoleSkillService;
import com.zaca.stillstanding.service.TeamService;

/**
 * @author hundun
 * Created on 2021/06/28
 */
@Component
public class MatchStrategyFactory {
    
    @Autowired
    private QuestionService questionService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private RoleSkillService roleSkillService;
    @Autowired
    private BuffService buffService;
    
    public BaseMatchStrategy getMatchStrategy(MatchStrategyType type) throws StillStandingException {
        switch (type) {
            case ENDLESS:
                return new EndlessStrategy(questionService, teamService, roleSkillService, buffService);
            case PRE:
                return new PreStrategy(questionService, teamService, roleSkillService, buffService);
            case MAIN:
                return new MainStrategy(questionService, teamService, roleSkillService, buffService);
            default:
                throw new NotFoundException("MatchStrategyType", type.name());
        }
    }
}
