package hundun.quizgame.server;


import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.InitBinder;

import hundun.quizgame.core.context.QuizCoreContext;
import hundun.quizgame.core.exception.ConflictException;
import hundun.quizgame.core.exception.NotFoundException;
import hundun.quizgame.core.exception.QuestionFormatException;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.domain.Question;
import hundun.quizgame.core.model.domain.TeamRuntimeModel;
import hundun.quizgame.core.prototype.match.AnswerType;
import hundun.quizgame.core.prototype.match.MatchConfig;
import hundun.quizgame.core.prototype.match.MatchStrategyType;
import hundun.quizgame.core.service.BuiltinDataConfiguration;
import hundun.quizgame.core.service.GameService;
import hundun.quizgame.core.service.QuestionLoaderService;
import hundun.quizgame.core.service.QuestionService;
import hundun.quizgame.core.service.SessionService;
import hundun.quizgame.core.service.TeamService;
import hundun.quizgame.core.tool.TextHelper;
import hundun.quizgame.core.view.match.MatchSituationView;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleQuestionTest {
	
	QuestionService questionService = QuizCoreContext.getInstance().questionService;
    
	TeamService teamService = QuizCoreContext.getInstance().teamService;
    
    SessionService sessionService = QuizCoreContext.getInstance().sessionService;
    
    GameService gameService = QuizCoreContext.getInstance().gameService;

	
	@Test
	public void test() throws IOException, QuizgameException {
		
		MatchSituationView matchSituation;
		
        // request_0
		MatchConfig matchConfig = new MatchConfig();
		matchConfig.setTeamNames(Arrays.asList(BuiltinDataConfiguration.DEMO_LIST_TEAM_NAME_0));
		matchConfig.setQuestionPackageName(QuestionLoaderService.TEST_PACKAGE_NAME);
		matchConfig.setMatchStrategyType(MatchStrategyType.ENDLESS);
		String sessionId = gameService.createMatch(matchConfig).getId();
		
		// request_1
        gameService.startMatch(sessionId);
        
        // request_2
        matchSituation = gameService.nextQustion(sessionId);
        
        // request_3
        String correctAnswer = TextHelper.intToAnswerText(matchSituation.getQuestion().getAnswer());
        matchSituation = gameService.teamAnswer(sessionId, correctAnswer);
        assertEquals(AnswerType.CORRECT, matchSituation.getAnswerResultEvent().getResult());
        assertEquals(1, matchSituation.getAnswerResultEvent().getAddScore());
	}


}
