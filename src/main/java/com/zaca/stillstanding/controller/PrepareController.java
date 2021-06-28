package com.zaca.stillstanding.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zaca.stillstanding.api.spring.IPrepareApi;
import com.zaca.stillstanding.core.role.RolePrototype;
import com.zaca.stillstanding.core.team.TeamPrototype;
import com.zaca.stillstanding.dto.ApiResult;
import com.zaca.stillstanding.dto.role.RoleConstInfoDTO;
import com.zaca.stillstanding.dto.team.TeamConstInfoDTO;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.service.GameService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.RoleSkillService;
import com.zaca.stillstanding.service.TeamService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/06/28
 */
@Slf4j
@RestController
@RequestMapping("/api/prepare")
public class PrepareController implements IPrepareApi {
    
    @Autowired
    QuestionService questionService;
    
    @Autowired
    GameService gameService;
    
    @Autowired
    TeamService teamService;
    
    @Autowired
    RoleSkillService roleSkillService;
    
    @Override
    public ApiResult<List<TeamConstInfoDTO>> listTeams() {
        log.info("===== listTeams =====");
        
            Collection<TeamPrototype> teamRuntimes = teamService.listTeams();
            List<TeamConstInfoDTO> teamConstInfoDTOs = new ArrayList<>();
            teamRuntimes.forEach(team -> teamConstInfoDTOs.add(team.toTeamConstInfoDTO()));
            return new ApiResult<>(teamConstInfoDTOs);
        
    }
    
    @Override
    public ApiResult<List<TeamConstInfoDTO>> updateTeam(
            TeamConstInfoDTO teamConstInfoDTO
            ) {
        log.info("===== updateTeam {} =====", teamConstInfoDTO);
        // TODO 中途失败回滚
        
        try {
            if (!teamService.existTeam(teamConstInfoDTO.getName())) {
                teamService.creatTeam(teamConstInfoDTO.getName());
            }
            teamService.setPickTagsForTeam(teamConstInfoDTO.getName(), teamConstInfoDTO.getPickTags());
            teamService.setBanTagsForTeam(teamConstInfoDTO.getName(), teamConstInfoDTO.getBanTags());
            teamService.setRoleForTeam(teamConstInfoDTO.getName(), teamConstInfoDTO.getRoleName());
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
        return listTeams();
    }

    @Override
    public ApiResult<Set<String>> listTags(String questionPackageName) {
        Set<String> tags;
        try {
            tags = questionService.getTags(questionPackageName);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
        return new ApiResult<>(tags);
    }

    @Override
    public ApiResult<List<RoleConstInfoDTO>> listRoles() {
        Collection<RolePrototype> prototypes = roleSkillService.listRoles();
        List<RoleConstInfoDTO> dtos = new ArrayList<>();
        prototypes.forEach(prototype -> dtos.add(prototype.toRoleConstInfoDTO()));
        return new ApiResult<>(dtos);
    }
}
