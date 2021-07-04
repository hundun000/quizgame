package hundun.quizgame.server.api.spring;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hundun.quizgame.core.dto.ApiResult;
import hundun.quizgame.core.dto.role.RoleConstInfoDTO;
import hundun.quizgame.core.dto.team.TeamConstInfoDTO;

/**
 * @author hundun
 * Created on 2021/06/28
 */
public interface IPrepareApi {
    @RequestMapping(
            value = "/listTeams", 
            method = RequestMethod.GET
            )
    ApiResult<List<TeamConstInfoDTO>> listTeams(
            
            );
    
    @RequestMapping(
            value = "/listTags", 
            method = RequestMethod.GET
            )
    ApiResult<Set<String>> listTags(
            @RequestParam(value = "questionPackageName") String questionPackageName
            );
    
    @RequestMapping(
            value = "/listRoles", 
            method = RequestMethod.GET
            )
    ApiResult<List<RoleConstInfoDTO>> listRoles(
            
            );
    
    @RequestMapping(value="/updateTeam", method=RequestMethod.POST)
    ApiResult<List<TeamConstInfoDTO>> updateTeam(
            @RequestBody TeamConstInfoDTO teamConstInfoDTO
            );
}
