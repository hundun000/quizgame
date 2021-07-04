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

import hundun.quizgame.core.dto.ApiResult;
import hundun.quizgame.core.dto.match.MatchConfigDTO;
import hundun.quizgame.core.dto.match.MatchSituationDTO;
import hundun.quizgame.core.dto.team.TeamConstInfoDTO;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.team.TeamPrototype;
import hundun.quizgame.core.model.team.TeamRuntime;
import hundun.quizgame.core.service.GameService;
import hundun.quizgame.core.service.QuestionLoaderService;
import hundun.quizgame.core.service.QuestionService;
import hundun.quizgame.core.service.TeamService;
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
    
    @Autowired
    QuestionLoaderService questionLoaderService;
    
    public GameController() {
        
    }
    
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    
    @Autowired
    QuestionService questionService;
    
    @Autowired
    GameService gameService;
    
    @Autowired
    TeamService teamService;

    @Override
    public ApiResult<MatchSituationDTO> createMatch(
            MatchConfigDTO matchConfigDTO
            ) {
        logger.info("===== /createPreMatch {} =====", matchConfigDTO);

        try {
            MatchSituationDTO matchSituationDTO = gameService.createMatch(matchConfigDTO);
            return new ApiResult<>(matchSituationDTO);
        } catch (QuizgameException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
    }
    
    
    
    
    
    
    @Override
    public ApiResult<MatchSituationDTO> start(
            String sessionId
            ) {
        logger.info("===== /start {}=====", sessionId);
        
        try {
            MatchSituationDTO matchSituationDTO = gameService.startMatch(sessionId);
            return new ApiResult<>(matchSituationDTO);
        } catch (QuizgameException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
        
    }
    
    
    @Override
    public ApiResult<MatchSituationDTO> teamAnswer(
            String sessionId,
            String answer
            ) {
        logger.info("===== /answer {} {} =====", sessionId, answer);
        try {
            MatchSituationDTO matchSituationDTO = gameService.teamAnswer(sessionId, answer);
            return new ApiResult<>(matchSituationDTO);
        } catch (QuizgameException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
    }
    

    @Override
    public ApiResult<MatchSituationDTO> teamUseSkill(
            String sessionId,
            String skillName
            ) {
        logger.info("===== /use-skill {} {} =====", sessionId, skillName);
        try {
            MatchSituationDTO matchSituationDTO = gameService.teamUseSkill(sessionId, skillName);
            return new ApiResult<>(matchSituationDTO);
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
    public ApiResult<MatchSituationDTO> nextQustion(String sessionId) {
        logger.info("===== /nextQustion {}=====", sessionId);
        
        try {
            MatchSituationDTO matchSituationDTO = gameService.nextQustion(sessionId);
            return new ApiResult<>(matchSituationDTO);
        } catch (QuizgameException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
    }

    
    
}
