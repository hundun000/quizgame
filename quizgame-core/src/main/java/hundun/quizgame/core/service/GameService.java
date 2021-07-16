package hundun.quizgame.core.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.SessionDataPackage;
import hundun.quizgame.core.model.domain.TeamModel;
import hundun.quizgame.core.model.domain.match.BaseMatch;
import hundun.quizgame.core.model.domain.match.MatchRecord;
import hundun.quizgame.core.model.domain.match.strategy.BaseMatchStrategy;
import hundun.quizgame.core.model.domain.match.strategy.MatchStrategyFactory;
import hundun.quizgame.core.prototype.match.MatchConfig;
import hundun.quizgame.core.view.match.MatchSituationView;
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

    
    
    public MatchSituationView createMatch(MatchConfig matchConfig) throws QuizgameException {
        log.info("createEndlessMatch by {}", matchConfig);
        
        BaseMatchStrategy strategy = matchStrategyFactory.getMatchStrategy(matchConfig.getMatchStrategyType());
        
        SessionDataPackage sessionDataPackage = sessionService.createSession(matchConfig.getQuestionPackageName());
        
        BaseMatch match = new BaseMatch(sessionDataPackage.getSessionId(), strategy);
        List<TeamModel> teamModels = new ArrayList<>();
        for (String teamName : matchConfig.getTeamNames()) {
            teamModels.add(new TeamModel(teamService.getTeam(teamName)));
        }
        match.initTeams(teamModels);
        
        sessionDataPackage.setMatch(match);
        
        log.info("match created, id = {}", match.getSessionId());
        MatchSituationView matchSituationView = match.toMatchSituationView();
        return matchSituationView;
    }
    

    

    
    public MatchSituationView startMatch(String sessionId) throws QuizgameException {
        log.info("start match:{}", sessionId);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.start();
        MatchSituationView matchSituationView = match.toMatchSituationView();
        return matchSituationView;
    }
    
    public MatchSituationView nextQustion(String sessionId) throws QuizgameException {
        log.info("nextQustion:{}", sessionId);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.nextQustion();
        MatchSituationView matchSituationView = match.toMatchSituationView();
        return matchSituationView;
    }
    
    private void finishMatch(BaseMatch match) throws QuizgameException {
        matchRecords.put(match.getSessionId(), new MatchRecord(match));
        
    }
    
    
    public MatchSituationView teamAnswer(String sessionId, String answer) throws QuizgameException {
        log.info("teamAnswer match:{}, answer = {}", sessionId, answer);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.teamAnswer(answer);
        if (match.finishEvent != null) {
            finishMatch(match);
        }
        MatchSituationView matchSituationView = match.toMatchSituationView();
        return matchSituationView;
    }
	
    public MatchSituationView teamUseSkill(String sessionId, String skillName) throws QuizgameException {
        log.info("teamUseSkill match:{}, skillName = {}", sessionId, skillName);
        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        BaseMatch match = sessionDataPackage.getMatch();
        match.teamUseSkill(skillName);
        MatchSituationView matchSituationView = match.toMatchSituationView();
        return matchSituationView;
    }

    
    

}
