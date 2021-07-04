package hundun.quizgame.server.api.spring;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hundun.quizgame.core.dto.ApiResult;
import hundun.quizgame.core.dto.match.MatchConfigDTO;
import hundun.quizgame.core.dto.match.MatchSituationDTO;
import hundun.quizgame.core.dto.team.TeamConstInfoDTO;

/**
 * @author hundun
 * Created on 2021/05/10
 */
public interface IGameApi {

    @RequestMapping(
            value = "/createMatch", 
            method = RequestMethod.POST
            )
    ApiResult<MatchSituationDTO> createMatch(
            @RequestBody MatchConfigDTO matchConfigDTO
            );
    
    
    @RequestMapping(
            value = "/start", 
            method = RequestMethod.POST
    )
    ApiResult<MatchSituationDTO> start(@RequestParam(value = "sessionId") String sessionId);
    
    @RequestMapping(
            value = "/nextQustion", 
            method = RequestMethod.POST
    )
    ApiResult<MatchSituationDTO> nextQustion(
            @RequestParam(value = "sessionId") String sessionId
            );
    
    @RequestMapping(
            value = "/answer", 
            method = RequestMethod.POST
    )
    ApiResult<MatchSituationDTO> teamAnswer(
            @RequestParam(value = "sessionId") String sessionId,
            @RequestParam(value = "answer") String answer
            );
    
    @RequestMapping(value="/use-skill", method=RequestMethod.POST)
    ApiResult<MatchSituationDTO> teamUseSkill(
            @RequestParam(value = "sessionId") String sessionId,
            @RequestParam(value = "skillName") String skillName
            );
    
    
}
