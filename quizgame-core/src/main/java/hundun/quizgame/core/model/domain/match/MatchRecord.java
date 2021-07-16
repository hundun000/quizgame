package hundun.quizgame.core.model.domain.match;
/**
 * @author hundun
 * Created on 2020/01/10
 */

import java.util.LinkedHashMap;
import java.util.Map;

public class MatchRecord {
    
    private String matchId;
    private Map<String, Integer> scores = new LinkedHashMap<>();
    
    public MatchRecord(BaseMatch match) {
        this.matchId = match.getSessionId();
        match.teamModels.forEach(team -> scores.put(team.getName(), team.getMatchScore()));
    }
    
    public String getMatchId() {
        return matchId;
    }
    
    public Map<String, Integer> getScores() {
        return scores;
    }

}
