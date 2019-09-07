package com.zaca.stillstanding.tool;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.match.CoupleMatch;
import com.zaca.stillstanding.domain.question.Question;
import com.zaca.stillstanding.domain.team.Team;

/**
 * @author hundun
 * Created on 2019/09/05
 */
public class FormatTool {
    
    public static JSONObject coupleMatchTOJSON(CoupleMatch match) {
        JSONObject object = new JSONObject();
        object.put("question", questionToJSON(match.getCurrentQuestion()));
        object.put("teamIndex", match.getCurrentTeamIndex());
        object.put("teams", teamsToJSON(match.getTeams()));
        object.put("events", match.getEvents());
        return object;
    }
    
    private static JSONObject questionToJSON(Question question) {
        JSONObject object = new JSONObject();
        // TODO
        return JSONObject.parseObject(JSON.toJSONString(question));
    }
    
    private static JSONArray teamsToJSON(List<Team> teams) {
        JSONArray array = new JSONArray();
        for (Team team : teams) {
            array.add(team.getMatchData());
        }
        
        return array;
    }

}
