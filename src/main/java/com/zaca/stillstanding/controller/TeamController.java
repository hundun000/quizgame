package com.zaca.stillstanding.controller;

import java.util.ArrayList;
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
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaca.stillstanding.domain.dto.ApiResult;
import com.zaca.stillstanding.domain.dto.IApiResult;
import com.zaca.stillstanding.domain.dto.TeamDTO;
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
    ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    TeamService teamService;
    
    @RequestMapping(value="", method=RequestMethod.GET)
    public IApiResult listTeams() throws JsonProcessingException {
        Collection<Team> teams = teamService.listTeams();
        List<TeamDTO> teamDTOs = new ArrayList<>();
        teams.forEach(team -> teamDTOs.add(team.toTeamDTO()));
        return new ApiResult(objectMapper.writeValueAsString(teamDTOs));
    }
    
    @RequestMapping(value="", method=RequestMethod.POST)
    public IApiResult updateTeam(
            @RequestParam(value = "team") TeamDTO teamDTO
            ) {
        logger.info("===== \"\" {} =====", teamDTO);
        // TODO 中途失败回滚
        
        try {
            teamService.setPickTagsForTeam(teamDTO.getName(), teamDTO.getPickTags());
            teamService.setBanTagsForTeam(teamDTO.getName(), teamDTO.getBanTags());
            teamService.setRoleForTeam(teamDTO.getName(), teamDTO.getRoleName());
        } catch (NotFoundException e) {
            return e;
        }
        return new ApiResult();
    }

}
