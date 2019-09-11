package com.zaca.stillstanding.domain.match;

import java.util.Collection;

import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.team.Team;

class MatchEvent{
	private final EventResultType type;
	private final JSONObject data;
	
	public MatchEvent(EventResultType type, JSONObject data) {
		this.type = type;
		this.data = data;
	}
	
	
	
	public static boolean isTypeInCollection(Collection<MatchEvent> events, EventResultType type) {
        for (MatchEvent event : events) {
            if (event.type == type) {
                return true;
            }
        }
        return false;
    }
	
	public static MatchEvent getTypeSwitchTeam(Team lastTeam, Team currentTeam) {
		JSONObject data = new JSONObject();
		data.put("last_team", lastTeam.getMatchData());
		data.put("current_team", currentTeam.getMatchData());
		return new MatchEvent(EventResultType.SWITCH_TEAM, data);
	}
	
	public static MatchEvent getTypeSwitchQuestion(int time) {
        JSONObject data = new JSONObject();
        data.put("time", time);
        return new MatchEvent(EventResultType.SWITCH_QUESTION, data);
    }
	
	public static MatchEvent getTypeTeamDie(Team team) {
        JSONObject data = new JSONObject();
        data.put("team", team.getMatchData());
        return new MatchEvent(EventResultType.TEAM_DIE, data);
    }
	
	public static MatchEvent getTypeFinish() {
        JSONObject data = new JSONObject();
        return new MatchEvent(EventResultType.FINISH, data);
    }
	
	
	public JSONObject getData() {
        return data;
    }
	public EventResultType getType() {
        return type;
    }
}