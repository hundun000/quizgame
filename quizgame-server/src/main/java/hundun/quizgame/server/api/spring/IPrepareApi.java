package hundun.quizgame.server.api.spring;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hundun.quizgame.core.prototype.RolePrototype;
import hundun.quizgame.core.prototype.TeamPrototype;
import hundun.quizgame.core.view.ApiResult;


/**
 * @author hundun
 * Created on 2021/06/28
 */
public interface IPrepareApi {
    @RequestMapping(
            value = "/listTeams", 
            method = RequestMethod.GET
            )
    ApiResult<List<TeamPrototype>> listTeams(
            
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
    ApiResult<List<RolePrototype>> listRoles(
            
            );
    
    @RequestMapping(value="/updateTeam", method=RequestMethod.POST)
    ApiResult<List<TeamPrototype>> updateTeam(
            @RequestBody TeamPrototype teamPrototypeSimpleView
            );
}
