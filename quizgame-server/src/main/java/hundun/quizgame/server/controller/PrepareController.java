package hundun.quizgame.server.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.prototype.RolePrototype;
import hundun.quizgame.core.prototype.TeamPrototype;
import hundun.quizgame.core.service.GameService;
import hundun.quizgame.core.service.QuestionService;
import hundun.quizgame.core.service.RoleSkillService;
import hundun.quizgame.core.service.TeamService;
import hundun.quizgame.core.view.ApiResult;
import hundun.quizgame.server.api.spring.IPrepareApi;
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
    public ApiResult<List<TeamPrototype>> listTeams() {
        log.info("===== listTeams =====");
        
        List<TeamPrototype> teamRuntimes = teamService.listTeams();

        return new ApiResult<>(teamRuntimes);
        
    }
    
    @Override
    public ApiResult<List<TeamPrototype>> updateTeam(
            TeamPrototype teamPrototypeSimpleView
            ) {
        log.info("===== updateTeam {} =====", teamPrototypeSimpleView);
        // TODO 中途失败回滚
        
//        try {
//            if (!teamService.existTeam(teamPrototypeSimpleView.getName())) {
//                teamService.registerTeam(teamPrototypeSimpleView.getName());
//            }
//            teamService.setPickTagsForTeam(teamPrototypeSimpleView.getName(), teamPrototypeSimpleView.getPickTags());
//            teamService.setBanTagsForTeam(teamPrototypeSimpleView.getName(), teamPrototypeSimpleView.getBanTags());
//            teamService.setRoleForTeam(teamPrototypeSimpleView.getName(), teamPrototypeSimpleView.getRolePrototype() getRoleName());
//        } catch (QuizgameException e) {
//            e.printStackTrace();
//            return new ApiResult<>(e.getMessage(), e.getRetcode());
//        }
        return listTeams();
    }

    @Override
    public ApiResult<Set<String>> listTags(String questionPackageName) {
        Set<String> tags;
        try {
            tags = questionService.getTags(questionPackageName);
        } catch (QuizgameException e) {
            e.printStackTrace();
            return new ApiResult<>(e.getMessage(), e.getRetcode());
        }
        return new ApiResult<>(tags);
    }

    @Override
    public ApiResult<List<RolePrototype>> listRoles() {
        Collection<RolePrototype> prototypes = roleSkillService.listRoles();
        List<RolePrototype> dtos = new ArrayList<>();
        prototypes.forEach(prototype -> dtos.add(prototype));
        return new ApiResult<>(dtos);
    }
}
