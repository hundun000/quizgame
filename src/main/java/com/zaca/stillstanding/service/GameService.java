package com.zaca.stillstanding.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zaca.stillstanding.domain.event.MatchEvent;
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
    QuestionService questionService;
    
    @Autowired
    TeamService teamService;
    
    @Autowired
    PreMatch match;
    
    public GameService() {
        
    }
    
    @PostConstruct
    private void post() {
        try {
            initOtherServiceForTest();
        } catch (StillStandingException e) {
            e.printStackTrace();
        }
    }

    public void initOtherServiceForTest() throws StillStandingException {
        questionService.initQuestions(QuestionTool.TEST_SMALL_PACKAGE_NAME);
        teamService.quickRegisterTeam("砍口垒同好组", "单机游戏", "动画", "ZACA娘");
        match.addTeams("砍口垒同好组");
    }
    
    public PreMatch getMatch() {
        return match;
    }
	


}
