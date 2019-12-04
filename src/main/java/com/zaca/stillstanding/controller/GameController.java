package com.zaca.stillstanding.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.zaca.stillstanding.domain.ApiResult;
import com.zaca.stillstanding.domain.IApiResult;
import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.match.BaseMatch;
import com.zaca.stillstanding.domain.match.PreMatch;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.service.GameService;
import com.zaca.stillstanding.tool.FormatTool;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
@RestController
@RequestMapping("/api/match")
public class GameController {
    
    @Autowired
    GameService gameService;
    
    @RequestMapping(value="/new", method=RequestMethod.POST)
    public IApiResult createMatch(
            @RequestParam(value = "teamNames") String[] teamNames
            ) {
        BaseMatch match = gameService.createMatch();
        try {
            match.setTeamsByNames(teamNames);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult(e);
        }
        return new ApiResult(match.getId());
    }
    
    @RequestMapping(value="/start", method=RequestMethod.POST)
    public IApiResult start(
            @RequestParam(value = "matchId") String matchId
            ) {
        BaseMatch match;
        try {
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
