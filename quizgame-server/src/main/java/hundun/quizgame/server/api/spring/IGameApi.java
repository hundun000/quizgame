package hundun.quizgame.server.api.spring;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hundun.quizgame.core.prototype.match.MatchConfig;
import hundun.quizgame.core.view.ApiResult;
import hundun.quizgame.core.view.match.MatchSituationView;


/**
 * @author hundun
 * Created on 2021/05/10
 */
public interface IGameApi {

    @RequestMapping(
            value = "/createMatch", 
            method = RequestMethod.POST
            )
    ApiResult<MatchSituationView> createMatch(
            @RequestBody MatchConfig matchConfig
            );
    
    
    @RequestMapping(
            value = "/start", 
            method = RequestMethod.POST
    )
    ApiResult<MatchSituationView> start(@RequestParam(value = "sessionId") String sessionId);
    
    @RequestMapping(
            value = "/nextQustion", 
            method = RequestMethod.POST
    )
    ApiResult<MatchSituationView> nextQustion(
            @RequestParam(value = "sessionId") String sessionId
            );
    
    @RequestMapping(
            value = "/answer", 
            method = RequestMethod.POST
    )
    ApiResult<MatchSituationView> teamAnswer(
            @RequestParam(value = "sessionId") String sessionId,
            @RequestParam(value = "answer") String answer
            );
    
    @RequestMapping(value="/use-skill", method=RequestMethod.POST)
    ApiResult<MatchSituationView> teamUseSkill(
            @RequestParam(value = "sessionId") String sessionId,
            @RequestParam(value = "skillName") String skillName
            );
    
    
}
