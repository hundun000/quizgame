package com.zaca.stillstanding.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.match.BaseMatch;
import com.zaca.stillstanding.domain.match.PreMatch;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.tool.QuestionTool;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
@Service
public class GameService {
	
    @Autowired
    private QuestionService questionService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private RoleSkillService roleSkillService;
    
    Map<String, BaseMatch> matches = new HashMap<>();
    
    public GameService() {
    }
    

    public String initOtherServiceForTest() throws StillStandingException {
        questionService.initQuestions(QuestionTool.TEST_SMALL_PACKAGE_NAME);
        teamService.quickRegisterTeam("砍口垒同好组", "单机游戏", "动画", "ZACA娘");
        
        BaseMatch match = createMatch();
        match.setTeamsByNames("砍口垒同好组");
        return match.getId();
    }
    
    public BaseMatch createMatch() {
        BaseMatch match = new PreMatch(questionService, teamService, roleSkillService);
        matches.put(match.getId(), match);
        return match;
    }
    
    public BaseMatch getMatch(String matchId) throws StillStandingException {
        BaseMatch match = matches.get(matchId);
        if (match == null) {
            throw new NotFoundException(matchId, matchId);
        }
        return match;
    }
	


}
