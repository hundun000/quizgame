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
import com.zaca.stillstanding.core.match.strategy.MainStrategy;
import com.zaca.stillstanding.core.match.strategy.MatchStrategyFactory;
import com.zaca.stillstanding.core.match.strategy.PreStrategy;
import com.zaca.stillstanding.core.team.TeamRuntime;
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
    private MatchStrategyFactory matchStrategyFactory;
    
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
        teamService.quickRegisterTeam("甲鱼队", Arrays.asList(), Arrays.asList(), "ZACA娘");
        teamService.quickRegisterTeam("沙丁鱼队", Arrays.asList(), Arrays.asList(), "ZACA娘");
        teamService.quickRegisterTeam("整合运动队", Arrays.asList(), Arrays.asList(), "ZACA娘");
        teamService.quickRegisterTeam("罗德岛队", Arrays.asList(), Arrays.asList(), "ZACA娘");
        
        teamService.quickRegisterTeam("砍口垒同好组", Arrays.asList("单机游戏"), Arrays.asList("动画"), "ZACA娘");
        teamService.quickRegisterTeam("方舟同好组", Arrays.asList("动画"), Arrays.asList("单机游戏"), "ZACA娘");
        teamService.quickRegisterTeam(TeamService.DEMO_TEAM_NAME, Arrays.asList(), Arrays.asList(), null);
    }
    
    public MatchSituationDTO createMatch(MatchConfigDTO matchConfigDTO) throws StillStandingException {
        logger.info("createEndlessMatch by {}", matchConfigDTO);
        
        BaseMatchStrategy strategy = matchStrategyFactory.getMatchStrategy(matchConfigDTO.getMatchStrategyType());
        
        SessionDataPackage sessionDataPackage = sessionService.createSession(matchConfigDTO.getQuestionPackageName());
        
        BaseMatch match = new BaseMatch(sessionDataPackage.getSessionId(), strategy);
        List<TeamRuntime> teamRuntimes = new ArrayList<>();
        for (String teamName : matchConfigDTO.getTeamNames()) {
            teamRuntimes.add(new TeamRuntime(teamService.getTeam(teamName)));
        }
        match.initTeams(teamRuntimes);
        
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

    
    

}
