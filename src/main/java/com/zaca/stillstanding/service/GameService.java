package com.zaca.stillstanding.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zaca.stillstanding.domain.event.EventType;
import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.match.BaseMatch;
import com.zaca.stillstanding.domain.match.MatchRecord;
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
	
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    
    @Autowired
    private QuestionService questionService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private RoleSkillService roleSkillService;
    
    Map<String, BaseMatch> matches = new HashMap<>();
    
    LinkedHashMap<String, MatchRecord> matchRecords = new LinkedHashMap<>();
    
    public GameService() {
    	
    }
    
    @PostConstruct
    public void post() {
    	try {
			initOtherServiceForTest();
		} catch (StillStandingException e) {
			e.printStackTrace();
		}
	}
    

    public void initOtherServiceForTest() throws StillStandingException {
        questionService.initQuestions(QuestionTool.PRELEASE_PACKAGE_NAME);
        teamService.quickRegisterTeam("砍口垒同好组", "单机游戏", "动画", "ZACA娘");
        teamService.quickRegisterTeam("方舟同好组", "动画", "单机游戏", "ZACA娘");
    }
    
    public BaseMatch createMatch(String[] teamNames) throws NotFoundException {
        logger.info("create match by teams={}", Arrays.toString(teamNames));
        BaseMatch match = new PreMatch(questionService, teamService, roleSkillService);
        matches.put(match.getId(), match);
        match.setTeamsByNames(teamNames);
        logger.info("match created, id = {}", match.getId());
        return match;
    }
    
    public BaseMatch startMatch(String matchId) throws StillStandingException {
        logger.info("start match:{}", matchId);
        BaseMatch match = getMatch(matchId);
        match.start();
        return match;
    }
    
    private void finishMatch(BaseMatch match) throws StillStandingException {
        matchRecords.put(match.getId(), new MatchRecord(match));
        matches.remove(match.getId());
    }
    
    public BaseMatch getMatch(String matchId) throws StillStandingException {
        BaseMatch match = matches.get(matchId);
        if (match == null) {
            throw new NotFoundException(matchId, matchId);
        }
        return match;
    }
    
    public BaseMatch teamAnswer(String matchId, String answer) throws StillStandingException {
        logger.info("teamAnswer match:{}, answer = {}", matchId, answer);
        BaseMatch match = getMatch(matchId);
        match.teamAnswer(answer);
        if (match.containsEventByType(EventType.FINISH)) {
            finishMatch(match);
        }
        return match;
    }
	
    public BaseMatch teamUseSkill(String matchId, String skillName) throws StillStandingException {
        logger.info("teamUseSkill match:{}, skillName = {}", matchId, skillName);
        BaseMatch match = getMatch(matchId);
        match.teamUseSkill(skillName);
        return match;
    }
    
    public List<MatchRecord> getMatchRecords() {
        return new ArrayList<>(matchRecords.values());
    }
    
    public MatchRecord getOneMatchRecord(String id) {
        return matchRecords.get(id);
    }

}
