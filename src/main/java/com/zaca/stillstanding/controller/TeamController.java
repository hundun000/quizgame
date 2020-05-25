package com.zaca.stillstanding.controller;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.ApiResult;
import com.zaca.stillstanding.domain.IApiResult;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.service.TeamService;

/**
 * @author hundun
 * Created on 2019/10/16
 */
@RestController
@RequestMapping("/api/teams")
public class TeamController {
    
    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);
    
    @Autowired
    TeamService teamService;
    
    @RequestMapping(value="", method=RequestMethod.GET)
    public IApiResult listTeams() {
        Collection<Team> teams = teamService.listTeams();
        JSONArray array = new JSONArray(teams.size());
        teams.forEach(team -> array.add(team.toAllDataPayload()));
        return new ApiResult(array);
    }
    
    @RequestMapping(value="", method=RequestMethod.POST)
    public IApiResult updateTeam(
            @RequestParam(value = "team") String teamString
            ) {
        logger.info("===== \"\" {} =====", teamString);
        // TODO 中途失败回滚
        
        JSONObject teamObject = JSONObject.parseObject(teamString);
        String teamName = teamObject.getString("name");
        try {
            teamService.setPickTagsForTeam(teamName, teamObject.getJSONArray("pickTags").toJavaList(String.class));
            teamService.setBanTagsForTeam(teamName, teamObject.getJSONArray("banTags").toJavaList(String.class));
            teamService.setRoleForTeam(teamName, teamObject.getString("roleName"));
        } catch (NotFoundException e) {
            return e;
        }
        return new ApiResult();
    }

}
