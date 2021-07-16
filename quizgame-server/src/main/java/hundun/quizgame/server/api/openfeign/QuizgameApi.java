package hundun.quizgame.server.api.openfeign;

import java.util.List;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import hundun.quizgame.core.prototype.TeamPrototype;
import hundun.quizgame.core.prototype.match.MatchConfig;
import hundun.quizgame.core.view.ApiResult;
import hundun.quizgame.core.view.match.MatchSituationView;

/**
 * @author hundun
 * Created on 2021/05/10
 */
public interface QuizgameApi {
    
    @RequestLine("POST /createEndlessMatch")
    @Headers("Content-Type: application/json")
    ApiResult<MatchSituationView> createEndlessMatch(
            MatchConfig matchConfig
            );
    
    @RequestLine("POST /createPreMatch")
    @Headers("Content-Type: application/json")
    ApiResult<MatchSituationView> createPreMatch(
            MatchConfig matchConfig
            );
    
    @RequestLine("POST /createMainMatch")
    @Headers("Content-Type: application/json")
    ApiResult<MatchSituationView> createMainMatch(
            MatchConfig matchConfig
            );
            
    
    @RequestLine("POST /start?sessionId={sessionId}")
    ApiResult<MatchSituationView> start(
            @Param("sessionId") String sessionId
            );
    
    @RequestLine("POST /nextQustion?sessionId={sessionId}")
    ApiResult<MatchSituationView> nextQustion(
            @Param("sessionId") String sessionId
            );
    
    @RequestLine("POST /answer?sessionId={sessionId}&answer={answer}")
    ApiResult<MatchSituationView> teamAnswer(
            @Param("sessionId") String sessionId,
            @Param("answer") String answer
            );
    
    @RequestLine("POST /use-skill?sessionId={sessionId}&skillName={skillName}")
    ApiResult<MatchSituationView> teamUseSkill(
            @Param("sessionId") String sessionId,
            @Param("skillName") String skillName
            );
    
    @RequestLine("GET /listTeams")
    ApiResult<List<TeamPrototype>> listTeams(
            
            );
    
    @RequestLine("POST /updateTeam")
    @Headers("Content-Type: application/json")
    ApiResult<List<TeamPrototype>> updateTeam(
            TeamPrototype teamPrototypeSimpleView
            );
}
