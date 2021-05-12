package com.zaca.stillstanding.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zaca.stillstanding.core.SessionDataPackage;
import com.zaca.stillstanding.core.match.BaseMatch;
import com.zaca.stillstanding.core.match.MatchRecord;
import com.zaca.stillstanding.core.match.strategy.BaseMatchStrategy;
import com.zaca.stillstanding.core.match.strategy.EndlessStrategy;
import com.zaca.stillstanding.core.match.strategy.PreStrategy;
import com.zaca.stillstanding.core.team.Team;
import com.zaca.stillstanding.dto.match.MatchConfigDTO;
import com.zaca.stillstanding.dto.match.MatchSituationDTO;
import com.zaca.stillstanding.exception.StillStandingException;

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
        
        teamService.quickRegisterTeam("砍口垒同好组", Arrays.asList("单机游戏"), Arrays.asList("动画"), "ZACA娘");
        teamService.quickRegisterTeam("方舟同好组", Arrays.asList("动画"), Arrays.asList("单机游戏"), "ZACA娘");
        teamService.quickRegisterTeam("游客", Arrays.asList(), Arrays.asList(), "ZACA娘");
    }
    
    public MatchSituationDTO createEndlessMatch(MatchConfigDTO matchConfigDTO) throws StillStandingException {
        logger.info("createEndlessMatch by {}", matchConfigDTO);
        
        EndlessStrategy strategy = new EndlessStrategy(questionService, teamService, roleSkillService, buffService);
        
        BaseMatch match = createMatch(strategy, matchConfigDTO);
        
        logger.info("match created, id = {}", match.getSessionId());
        MatchSituationDTO matchSituationDTO = match.toMatchSituationDTO();
        return matchSituationDTO;
    }
    
    
    private BaseMatch createMatch(BaseMatchStrategy strategy, MatchConfigDTO matchConfigDTO) throws StillStandingException {
        SessionDataPackage sessionDataPackage = sessionService.createSession(matchConfigDTO.getQuestionPackageName());
        
        BaseMatch match = new BaseMatch(sessionDataPackage.getSessionId(), strategy);
        List<Team> teams = new ArrayList<>();
        for (String teamName : matchConfigDTO.getTeamNames()) {
            teams.add(teamService.getTeam(teamName));
        }
        match.initTeams(teams);
        
        sessionDataPackage.setMatch(match);
        
        return match;
    }
    
    public MatchSituationDTO createPreMatch(MatchConfigDTO matchConfigDTO) throws StillStandingException {
        logger.info("createPreMatch by {}", matchConfigDTO);
        
        PreStrategy strategy = new PreStrategy(questionService, teamService, roleSkillService, buffService);
        
        BaseMatch match = createMatch(strategy, matchConfigDTO);
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
