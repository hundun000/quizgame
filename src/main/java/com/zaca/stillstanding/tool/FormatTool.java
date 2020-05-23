package com.zaca.stillstanding.tool;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.buff.RunTimeBuff;
import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.match.BaseMatch;
import com.zaca.stillstanding.domain.question.Question;
import com.zaca.stillstanding.domain.team.Team;

/**
 * @author hundun
 * Created on 2019/09/05
 */
public class FormatTool {
    
    public static JSONObject matchToJSON(BaseMatch match) {
        JSONObject object = new JSONObject();
        object.put("id", match.getId());
        object.put("question", questionToJSON(match.getCurrentQuestion()));
        object.put("currentTeamIndex", match.getCurrentTeamIndex());
        //object.put("teams", teamsToJSON(match.getTeams()));
        object.put("events", match.getEvents());
        object.put("currentTeamBuffsData", buffsToJSON(match.getCurrentTeam().getBuffs()));
        return object;
    }
    
    public static JSONObject buffsToJSON(List<RunTimeBuff> buffs) {
        JSONObject object = new JSONObject();
        for (RunTimeBuff buff : buffs) {
            object.put(buff.getModel().getName(), buff.getDuration());
        }
        return object;
    }
    
    public static JSONObject matchToShortJSON(BaseMatch match) {
        JSONObject object = new JSONObject();
        object.put("question", questionToShortJSON(match.getCurrentQuestion()));
        object.put("currentTeamIndex", match.getCurrentTeamIndex());
        object.put("teams", teamsToShortJSON(match.getTeams()).toJSONString());
        object.put("events", eventsToShortJSON(match.getEvents()));
        return object;
    }
    
    public static JSONObject questionToShortJSON(Question question) {
        JSONObject object = new JSONObject();
        object.put("stem&ans", question.getStem() + "=" + question.getAnswer());
        object.put("options", Arrays.toString(question.getOptions()));
        object.put("resource.data", question.getResource().getData());
        return object;
    }
    
    public static JSONObject questionToJSON(Question question) {
        JSONObject object = new JSONObject();
        // TODO
        return JSONObject.parseObject(JSON.toJSONString(question));
    }
    
    public static JSONArray teamsToJSON(List<Team> teams) {
        JSONArray array = new JSONArray();
        for (Team team : teams) {
            array.add(team.getMatchData());
        }
        
        return array;
    }
    
    public static JSONArray teamsToShortJSON(List<Team> teams) {
        JSONArray array = new JSONArray();
        for (Team team : teams) {
            array.add(team.getName());
        }
        
        return array;
    }
    
    public static JSONArray eventsToShortJSON(List<MatchEvent> events) {
        JSONArray array = new JSONArray();
        for (MatchEvent event : events) {
            array.add(JSON.toJSONString(event));
        }
        return array;
    }

}
