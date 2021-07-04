package hundun.quizgame.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hundun.quizgame.core.dto.match.MatchConfigDTO;
import hundun.quizgame.core.dto.match.MatchSituationDTO;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.SessionDataPackage;
import hundun.quizgame.core.model.match.BaseMatch;
import hundun.quizgame.core.model.match.MatchRecord;
import hundun.quizgame.core.model.match.strategy.BaseMatchStrategy;
import hundun.quizgame.core.model.match.strategy.EndlessStrategy;
import hundun.quizgame.core.model.match.strategy.MainStrategy;
import hundun.quizgame.core.model.match.strategy.MatchStrategyFactory;
import hundun.quizgame.core.model.match.strategy.PreStrategy;
import hundun.quizgame.core.model.team.TeamRuntime;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
@Slf4j
@Service
public class GameService {

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
		} catch (QuizgameException e) {
			e.printStackTrace();
		}
	}
    

    public void initOtherServiceForTest() throws QuizgameException {
        teamService.quickRegisterTeam("甲鱼队", Arrays.asList(), Arrays.asList(), "ZACA娘");
        teamService.quickRegisterTeam("沙丁鱼队", Arrays.asList(), Arrays.asList(), "ZACA娘");
        teamService.quickRegisterTeam("整合运动队", Arrays.asList(), Arrays.asList(), "ZACA娘");
        teamService.quickRegisterTeam("罗德岛队", Arrays.asList(), Arrays.asList(), "ZACA娘");
        
        teamService.quickRegisterTeam("砍口垒同好组", Arrays.asList("单机游戏"), Arrays.asList("动画"), "ZACA娘");
        teamService.quickRegisterTeam("方舟同好组", Arrays.asList("动画"), Arrays.asList("单机游戏"), "ZACA娘");
        teamService.quickRegisterTeam(TeamService.DEMO_TEAM_NAME, Arrays.asList(), Arrays.asList(), null);
    }
    
    public MatchSituationDTO createMatch(MatchConfigDTO matchConfigDTO) throws QuizgameException {
        log.info("createEndlessMatch by {}", matchConfigDTO);
        
        BaseMatchStrategy strategy = matchStrategyFactory.getMatchStrategy(matchConfigDTO.getMatchStrategyType());
        
        SessionDataPackage sessionDataPackage = sessionService.createSession(matchConfigDTO.getQuestionPackageName());
        
        BaseMatch match = new BaseMatch(sessionDataPackage.getSessionId(), strategy);
        List<TeamRuntime> teamRuntimes = new ArrayList<>();
        for (String teamName : matchConfigDTO.getTeamNames()) {
            teamRuntimes.add(new TeamRuntime(teamService.getTeam(teamName)));
        }
        match.initTeams(teamRuntimes);
        
        sessionDataPackage.setMatch(match);
        
        log.info("match created, id = {}", match.getSessionId());
        MatchSituationDTO matchSituationDTO = match.toMatchSituationDTO();
        return matchSituationDTO;
    }
    

    

    
    public MatchSituationDTO startMatch(String sessionId) throws QuizgameException {
        log.info("start match:{}", sessionId);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.start();
        MatchSituationDTO matchSituationDTO = match.toMatchSituationDTO();
        return matchSituationDTO;
    }
    
    public MatchSituationDTO nextQustion(String sessionId) throws QuizgameException {
        log.info("nextQustion:{}", sessionId);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.nextQustion();
        MatchSituationDTO matchSituationDTO = match.toMatchSituationDTO();
        return matchSituationDTO;
    }
    
    private void finishMatch(BaseMatch match) throws QuizgameException {
        matchRecords.put(match.getSessionId(), new MatchRecord(match));
        
    }
    
    
    public MatchSituationDTO teamAnswer(String sessionId, String answer) throws QuizgameException {
        log.info("teamAnswer match:{}, answer = {}", sessionId, answer);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.teamAnswer(answer);
        if (match.finishEvent != null) {
            finishMatch(match);
        }
        MatchSituationDTO matchSituationDTO = match.toMatchSituationDTO();
        return matchSituationDTO;
    }
	
    public MatchSituationDTO teamUseSkill(String sessionId, String skillName) throws QuizgameException {
        log.info("teamUseSkill match:{}, skillName = {}", sessionId, skillName);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.teamUseSkill(skillName);
        MatchSituationDTO matchSituationDTO = match.toMatchSituationDTO();
        return matchSituationDTO;
    }

    
    

}
