package com.zaca.stillstanding.controller;

import java.io.File;
import java.util.Arrays;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaca.stillstanding.api.StillstandingApi;
import com.zaca.stillstanding.domain.dto.ApiResult;
import com.zaca.stillstanding.domain.dto.IApiResult;
import com.zaca.stillstanding.domain.dto.MatchConfigDTO;
import com.zaca.stillstanding.domain.dto.MatchEvent;
import com.zaca.stillstanding.domain.dto.MatchSituationDTO;
import com.zaca.stillstanding.domain.match.BaseMatch;
import com.zaca.stillstanding.domain.match.MatchRecord;
import com.zaca.stillstanding.domain.match.PreMatch;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.service.GameService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.tool.FileTool;
import com.zaca.stillstanding.tool.QuestionTool;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
@RestController
@RequestMapping("/api/game")
public class GameController implements StillstandingApi {
    
    ObjectMapper objectMapper = new ObjectMapper();
    
    public static final String RESOURCE_ICON_FOLDER = "./data/pictures/";
    
    public GameController() {
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
    }
    
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    
    @Autowired
    QuestionService questionService;
    
    @Autowired
    GameService gameService;
    
    
    @RequestMapping(value="/createPreMatch", method=RequestMethod.POST)
    public IApiResult createPreMatch(
            @RequestBody MatchConfigDTO matchConfigDTO
            ) {
        logger.info("===== /createPreMatch {} =====", matchConfigDTO);

        try {
            MatchSituationDTO matchSituationDTO = gameService.createPreMatch(matchConfigDTO);
            String payload = objectMapper.writeValueAsString(matchSituationDTO);
            return new ApiResult(payload);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        }
        
    }
    
    @Override
    public ApiResult createEndlessMatch(
            MatchConfigDTO matchConfigDTO
            ) {
        logger.info("===== /createEndlessMatch {} =====", matchConfigDTO);
        try {
            MatchSituationDTO matchSituationDTO = gameService.createEndlessMatch(matchConfigDTO);
            String payload = objectMapper.writeValueAsString(matchSituationDTO);
            return new ApiResult(payload);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        }
        
    }
    
    @RequestMapping(value="/match-records/all", method=RequestMethod.GET)
    public IApiResult getMatchRecords() {
        List<MatchRecord> matchRecords = gameService.getMatchRecords();
        return new ApiResult(JSON.toJSONString(matchRecords));
    }
    
    @RequestMapping(value="/match-records", method=RequestMethod.GET)
    public IApiResult getOneMatchRecord(
            @RequestParam(value = "sessionId") String sessionId
            ) {
        logger.info("===== /match-records {} =====", sessionId);
        MatchRecord matchRecord = gameService.getOneMatchRecord(sessionId);
        return new ApiResult(JSON.toJSONString(matchRecord));
    }
    
    @Override
    public ApiResult start(
            String sessionId
            ) {
        logger.info("===== /start {}=====", sessionId);
        
        try {
            MatchSituationDTO matchSituationDTO = gameService.startMatch(sessionId);
            String payload = objectMapper.writeValueAsString(matchSituationDTO);
            return new ApiResult(payload);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        }
        
    }
    
    
    @Override
    public ApiResult teamAnswer(
            String sessionId,
            String answer
            ) {
        logger.info("===== /answer {} {} =====", sessionId, answer);
        try {
            MatchSituationDTO matchSituationDTO = gameService.teamAnswer(sessionId, answer);
            String payload = objectMapper.writeValueAsString(matchSituationDTO);
            return new ApiResult(payload);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        }
    }
    
    @RequestMapping(value="/use-skill", method=RequestMethod.POST)
    public IApiResult teamUseSkill(
            @RequestParam(value = "sessionId") String sessionId,
            @RequestParam(value = "skillName") String skillName
            ) {
        logger.info("===== /use-skill {} {} =====", sessionId, skillName);
        try {
            MatchSituationDTO matchSituationDTO = gameService.teamUseSkill(sessionId, skillName);
            String payload = objectMapper.writeValueAsString(matchSituationDTO);
            return new ApiResult(payload);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/pictures",method=RequestMethod.GET)
    public IApiResult pictures(HttpServletResponse response,
            @RequestParam("id") String id
            ) {
        logger.info("===== /pictures {} =====", id);
        String fileName = id;
        String filePathName = RESOURCE_ICON_FOLDER + id;

        File file = new File(filePathName);
        if (!file.exists()) {
            return new ApiResult("文件名找不到文件！" + file.getAbsolutePath());
        }
        try {
            FileTool.putFileToResponse(response, file, fileName);
        } catch (Exception e) {
            return new ApiResult(new StillStandingException(e.getMessage(), -1));
        }
        return new ApiResult("成功");
    }

    @Override
    public ApiResult nextQustion(String sessionId) {
        logger.info("===== /nextQustion {}=====", sessionId);
        
        try {
            MatchSituationDTO matchSituationDTO = gameService.nextQustion(sessionId);
            String payload = objectMapper.writeValueAsString(matchSituationDTO);
            return new ApiResult(payload);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        }
    }
    
}
