package com.zaca.stillstanding.controller;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaca.stillstanding.api.StillstandingApi;
import com.zaca.stillstanding.core.team.Team;
import com.zaca.stillstanding.dto.ApiResult;
import com.zaca.stillstanding.dto.match.MatchConfigDTO;
import com.zaca.stillstanding.dto.match.MatchSituationDTO;
import com.zaca.stillstanding.dto.team.TeamConstInfoDTO;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.service.GameService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.TeamService;
import com.zaca.stillstanding.tool.FileTool;

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
    
    @Autowired
    TeamService teamService;

    @Override
    public ApiResult<MatchSituationDTO> createMainMatch(
            MatchConfigDTO matchConfigDTO
            ) {
        logger.info("===== /createPreMatch {} =====", matchConfigDTO);

        try {
            MatchSituationDTO matchSituationDTO = gameService.createMainMatch(matchConfigDTO);
            return new ApiResult<>(matchSituationDTO);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
    }
    
    @Override
    public ApiResult<MatchSituationDTO> createPreMatch(
            MatchConfigDTO matchConfigDTO
            ) {
        logger.info("===== /createPreMatch {} =====", matchConfigDTO);

        try {
            MatchSituationDTO matchSituationDTO = gameService.createPreMatch(matchConfigDTO);
            return new ApiResult<>(matchSituationDTO);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
    }
    
    @Override
    public ApiResult<MatchSituationDTO> createEndlessMatch(
            MatchConfigDTO matchConfigDTO
            ) {
        logger.info("===== /createEndlessMatch {} =====", matchConfigDTO);
        try {
            MatchSituationDTO matchSituationDTO = gameService.createEndlessMatch(matchConfigDTO);
            return new ApiResult<>(matchSituationDTO);
        } catch (StillStandingException e) {
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
        } catch (StillStandingException e) {
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
        } catch (StillStandingException e) {
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
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/pictures",method=RequestMethod.GET)
    public ApiResult<String> pictures(HttpServletResponse response,
            @RequestParam("id") String id
            ) {
        logger.info("===== /pictures {} =====", id);
        String fileName = id;
        String filePathName = RESOURCE_ICON_FOLDER + id;

        File file = new File(filePathName);
        if (!file.exists()) {
            return new ApiResult<>("文件名找不到文件！" + file.getAbsolutePath());
        }
        try {
            FileTool.putFileToResponse(response, file, fileName);
        } catch (Exception e) {
            return new ApiResult<>(e.getMessage(), -1);
        }
        return new ApiResult<>("成功");
    }

    @Override
    public ApiResult<MatchSituationDTO> nextQustion(String sessionId) {
        logger.info("===== /nextQustion {}=====", sessionId);
        
        try {
            MatchSituationDTO matchSituationDTO = gameService.nextQustion(sessionId);
            return new ApiResult<>(matchSituationDTO);
        } catch (StillStandingException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
    }

    @Override
    public ApiResult<List<TeamConstInfoDTO>> listTeams() {
        logger.info("===== listTeams =====");
        
            Collection<Team> teams = teamService.listTeams();
            List<TeamConstInfoDTO> teamConstInfoDTOs = new ArrayList<>();
            teams.forEach(team -> teamConstInfoDTOs.add(team.toTeamDTO()));
            return new ApiResult<>(teamConstInfoDTOs);
        
    }
    
    @Override
    public ApiResult<List<TeamConstInfoDTO>> updateTeam(
            TeamConstInfoDTO teamConstInfoDTO
            ) {
        logger.info("===== updateTeam {} =====", teamConstInfoDTO);
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
    
}
