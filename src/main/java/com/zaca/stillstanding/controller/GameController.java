package com.zaca.stillstanding.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zaca.stillstanding.domain.ApiResult;
import com.zaca.stillstanding.domain.IApiResult;
import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.match.BaseMatch;
import com.zaca.stillstanding.domain.match.MatchRecord;
import com.zaca.stillstanding.domain.match.PreMatch;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.service.GameService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.tool.FormatTool;
import com.zaca.stillstanding.tool.QuestionTool;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
@RestController
@RequestMapping("/api/match")
public class GameController {
    
    public GameController() {
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
    }
    
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    
    @Autowired
    QuestionService questionService;
    
    @Autowired
    GameService gameService;
    
    @RequestMapping(value="/hello", method=RequestMethod.POST)
    public IApiResult hello() {
        return new ApiResult("hi");
    }
    
    @RequestMapping(value="/new", method=RequestMethod.POST)
    public IApiResult createMatch(
            @RequestParam(value = "teamNames") String[] teamNames
            ) {
        logger.info("===== /new {} =====", Arrays.toString(teamNames));
        try {
            BaseMatch match = gameService.createMatch(teamNames);
            return new ApiResult(match.getId());
        } catch (StillStandingException e) {
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
            @RequestParam(value = "matchId") String matchId
            ) {
        logger.info("===== /match-records {} =====", matchId);
        MatchRecord matchRecord = gameService.getOneMatchRecord(matchId);
        return new ApiResult(JSON.toJSONString(matchRecord));
    }
    
    @RequestMapping(value="/start", method=RequestMethod.POST)
    public IApiResult start(
            @RequestParam(value = "matchId") String matchId,
            @RequestParam(value = "reload_questions", required = false, defaultValue = "true") boolean reloadQuestions
            ) {
        logger.info("===== /start {} {} =====", matchId, reloadQuestions);
        BaseMatch match;
        try {
            if (reloadQuestions) {
                questionService.initQuestions(QuestionTool.PRELEASE_PACKAGE_NAME);
            }
            match = gameService.startMatch(matchId);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        }
        return new ApiResult(FormatTool.matchToJSON(match));
    }
    
    @RequestMapping(value="/answer", method=RequestMethod.POST)
    public IApiResult teamAnswer(
            @RequestParam(value = "matchId") String matchId,
            @RequestParam(value = "answer") String answer
            ) {
        logger.info("===== /answer {} {} =====", matchId, answer);
        BaseMatch match;
        try {
            match = gameService.teamAnswer(matchId, answer);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        }
        return new ApiResult(FormatTool.matchToJSON(match));
    }
    
    @RequestMapping(value="/use-skill", method=RequestMethod.POST)
    public IApiResult teamUseSkill(
            @RequestParam(value = "matchId") String matchId,
            @RequestParam(value = "skillName") String skillName
            ) {
        logger.info("===== /use-skill {} {} =====", matchId, skillName);
        BaseMatch match;
        try {
            match = gameService.teamUseSkill(matchId, skillName);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        }
        return new ApiResult(FormatTool.matchToJSON(match));
    }
}
