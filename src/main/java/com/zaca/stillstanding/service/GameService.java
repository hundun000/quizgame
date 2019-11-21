package com.zaca.stillstanding.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.match.BaseMatch;
import com.zaca.stillstanding.domain.match.PreMatch;
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
    
    
    BaseMatch match;
    
    public GameService() {
        }
    
    @PostConstruct
    private void post() {
        
        this.match = new PreMatch(questionService, teamService, roleSkillService);
        
        try {
            initOtherServiceForTest();
        } catch (StillStandingException e) {
            e.printStackTrace();
        }
    }

    private void initOtherServiceForTest() throws StillStandingException {
        questionService.initQuestions(QuestionTool.TEST_SMALL_PACKAGE_NAME);
        teamService.quickRegisterTeam("砍口垒同好组", "单机游戏", "动画", "ZACA娘");
        match.addTeams("砍口垒同好组");
    }
    
    public BaseMatch getMatch() {
        return match;
    }
	


}
