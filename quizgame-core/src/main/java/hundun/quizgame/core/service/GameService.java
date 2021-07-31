package hundun.quizgame.core.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import hundun.quizgame.core.context.IQuizCoreComponent;
import hundun.quizgame.core.context.QuizCoreContext;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.SessionDataPackage;
import hundun.quizgame.core.model.domain.TeamRuntimeModel;
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
public class GameService implements IQuizCoreComponent {

    private TeamService teamService;
    private MatchStrategyFactory matchStrategyFactory;
    private SessionService sessionService;
    
    LinkedHashMap<String, MatchRecord> matchRecords = new LinkedHashMap<>();
    
    public GameService() {
    	
    }
    
    @Override
    public void wire(QuizCoreContext quizCoreContext) {
        this.teamService = quizCoreContext.teamService;
        this.matchStrategyFactory = quizCoreContext.matchStrategyFactory;
        this.sessionService = quizCoreContext.sessionService;
    }
    
    
    public MatchSituationView createMatch(MatchConfig matchConfig) throws QuizgameException {
        log.info("createEndlessMatch by {}", matchConfig);
        
        BaseMatchStrategy strategy = matchStrategyFactory.getMatchStrategy(matchConfig.getMatchStrategyType());
        
        SessionDataPackage sessionDataPackage = sessionService.createSession(matchConfig.getQuestionPackageName());
        
        BaseMatch match = new BaseMatch(sessionDataPackage.getSessionId(), strategy);
        List<TeamRuntimeModel> teamRuntimeModels = new ArrayList<>();
        for (String teamName : matchConfig.getTeamNames()) {
            teamRuntimeModels.add(new TeamRuntimeModel(teamService.getTeam(teamName)));
        }
        match.initTeams(teamRuntimeModels);
        
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
