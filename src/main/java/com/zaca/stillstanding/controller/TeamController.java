package com.zaca.stillstanding.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.service.TeamService;

/**
 * @author hundun
 * Created on 2019/10/16
 */
@RestController
@RequestMapping("/api/teams")
public class TeamController {
    
    @Autowired
    TeamService teamService;
    
    @RequestMapping(value="", method=RequestMethod.GET)
    public Object listTeams() {
        Collection<Team> teams = teamService.listTeams();
        JSONArray array = new JSONArray(teams.size());
        teams.forEach(team -> array.add(team.getAllData()));
        return array;
    }
    
    @RequestMapping(value="", method=RequestMethod.POST)
    public Object test(
            @RequestParam(value = "teamName") String teamName
            ) {
        System.out.println(teamName);
        return null;
    }

}
