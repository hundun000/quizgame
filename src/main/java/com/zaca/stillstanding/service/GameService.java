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

import com.zaca.stillstanding.domain.SessionDataPackage;
import com.zaca.stillstanding.domain.dto.EventType;
import com.zaca.stillstanding.domain.dto.MatchConfigDTO;
import com.zaca.stillstanding.domain.dto.MatchEvent;
import com.zaca.stillstanding.domain.dto.MatchSituationDTO;
import com.zaca.stillstanding.domain.match.BaseMatch;
import com.zaca.stillstanding.domain.match.EndlessMatch;
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
    
    @Autowired
    private BuffService buffService;

    
    @Autowired
    private SessionService sessionService;
    
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
        
        teamService.quickRegisterTeam("砍口垒同好组", "单机游戏", "动画", "ZACA娘");
        teamService.quickRegisterTeam("方舟同好组", "动画", "单机游戏", "ZACA娘");
    }
    
    public MatchSituationDTO createEndlessMatch(MatchConfigDTO matchConfigDTO) throws StillStandingException {
        logger.info("createEndlessMatch by {}", matchConfigDTO);
        
        SessionDataPackage sessionDataPackage = sessionService.createSession(matchConfigDTO.getQuestionPackageName());
        EndlessMatch match = new EndlessMatch(questionService, teamService, roleSkillService, buffService, sessionDataPackage.getSessionId());
        match.setTeamsByNames(matchConfigDTO.getTeamNames());
        sessionDataPackage.setMatch(match);
        logger.info("match created, id = {}", match.getSessionId());
        MatchSituationDTO matchSituationDTO = match.toMatchSituationDTO();
        return matchSituationDTO;
    }
    
    public MatchSituationDTO createPreMatch(MatchConfigDTO matchConfigDTO) throws StillStandingException {
        logger.info("createPreMatch by {}", matchConfigDTO);
        
        SessionDataPackage sessionDataPackage = sessionService.createSession(matchConfigDTO.getQuestionPackageName());
        
        PreMatch match = new PreMatch(questionService, teamService, roleSkillService, buffService, sessionDataPackage.getSessionId());
        match.setTeamsByNames(matchConfigDTO.getTeamNames());
        sessionDataPackage.setMatch(match);
        logger.info("match created, id = {}", match.getSessionId());
        
        MatchSituationDTO matchSituationDTO = match.toMatchSituationDTO();
        return matchSituationDTO;
    }
    
    public MatchSituationDTO startMatch(String sessionId) throws StillStandingException {
        logger.info("start match:{}", sessionId);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.start();
        MatchSituationDTO matchSituationDTO = match.toMatchSituationDTO();
        return matchSituationDTO;
    }
    
    public MatchSituationDTO nextQustion(String sessionId) throws StillStandingException {
        logger.info("nextQustion:{}", sessionId);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.nextQustion();
        MatchSituationDTO matchSituationDTO = match.toMatchSituationDTO();
        return matchSituationDTO;
    }
    
    private void finishMatch(BaseMatch match) throws StillStandingException {
        matchRecords.put(match.getSessionId(), new MatchRecord(match));
        
    }
    
    
    public MatchSituationDTO teamAnswer(String sessionId, String answer) throws StillStandingException {
        logger.info("teamAnswer match:{}, answer = {}", sessionId, answer);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.teamAnswer(answer);
        if (match.finishEvent != null) {
            finishMatch(match);
        }
        MatchSituationDTO matchSituationDTO = match.toMatchSituationDTO();
        return matchSituationDTO;
    }
	
    public MatchSituationDTO teamUseSkill(String sessionId, String skillName) throws StillStandingException {
        logger.info("teamUseSkill match:{}, skillName = {}", sessionId, skillName);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.teamUseSkill(skillName);
        MatchSituationDTO matchSituationDTO = match.toMatchSituationDTO();
        return matchSituationDTO;
    }
    
    public List<MatchRecord> getMatchRecords() {
        return new ArrayList<>(matchRecords.values());
    }
    
    public MatchRecord getOneMatchRecord(String id) {
        return matchRecords.get(id);
    }

}
