package hundun.quizgame.server.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hundun.quizgame.core.context.QuizCoreContext;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.domain.TeamRuntimeModel;
import hundun.quizgame.core.prototype.TeamPrototype;
import hundun.quizgame.core.prototype.match.MatchConfig;
import hundun.quizgame.core.service.GameService;
import hundun.quizgame.core.service.QuestionLoaderService;
import hundun.quizgame.core.service.QuestionService;
import hundun.quizgame.core.service.TeamService;
import hundun.quizgame.core.view.ApiResult;
import hundun.quizgame.core.view.match.MatchSituationView;
import hundun.quizgame.server.api.spring.IGameApi;
import hundun.quizgame.server.utils.FileTool;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
@RestController
@RequestMapping("/api/game")
public class GameController implements IGameApi {
    
    ObjectMapper objectMapper = new ObjectMapper();
    
    private QuestionLoaderService questionLoaderService = QuizCoreContext.getInstance().questionLoaderService;
    
    private GameService gameService = QuizCoreContext.getInstance().gameService;

    
    public GameController() {
        
    }
    
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    
    

    @Override
    public ApiResult<MatchSituationView> createMatch(
            MatchConfig matchConfig
            ) {
        logger.info("===== /createPreMatch {} =====", matchConfig);

        try {
            MatchSituationView matchSituationView = gameService.createMatch(matchConfig);
            return new ApiResult<>(matchSituationView);
        } catch (QuizgameException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
    }
    
    
    
    
    
    
    @Override
    public ApiResult<MatchSituationView> start(
            String sessionId
            ) {
        logger.info("===== /start {}=====", sessionId);
        
        try {
            MatchSituationView matchSituationView = gameService.startMatch(sessionId);
            return new ApiResult<>(matchSituationView);
        } catch (QuizgameException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
        
    }
    
    
    @Override
    public ApiResult<MatchSituationView> teamAnswer(
            String sessionId,
            String answer
            ) {
        logger.info("===== /answer {} {} =====", sessionId, answer);
        try {
            MatchSituationView matchSituationView = gameService.teamAnswer(sessionId, answer);
            return new ApiResult<>(matchSituationView);
        } catch (QuizgameException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
    }
    

    @Override
    public ApiResult<MatchSituationView> teamUseSkill(
            String sessionId,
            String skillName
            ) {
        logger.info("===== /use-skill {} {} =====", sessionId, skillName);
        try {
            MatchSituationView matchSituationView = gameService.teamUseSkill(sessionId, skillName);
            return new ApiResult<>(matchSituationView);
        } catch (QuizgameException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/pictures",method=RequestMethod.GET)
    public void pictures(HttpServletResponse response,
            @RequestParam("id") String id
            ) {
        logger.info("===== /pictures {} =====", id);
        String fileName = id;
        String filePathName = questionLoaderService.RESOURCE_ICON_FOLDER + id;

        File file = new File(filePathName);
        if (!file.exists()) {
            logger.warn("文件名找不到文件！" + file.getAbsolutePath());
            return;
        }
        try {
            FileTool.putFileToResponse(response, file, fileName);
        } catch (Exception e) {
            e.printStackTrace();;
            return;
        }
        return;
    }

    @Override
    public ApiResult<MatchSituationView> nextQustion(String sessionId) {
        logger.info("===== /nextQustion {}=====", sessionId);
        
        try {
            MatchSituationView matchSituationView = gameService.nextQustion(sessionId);
            return new ApiResult<>(matchSituationView);
        } catch (QuizgameException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
    }

    
    
}
