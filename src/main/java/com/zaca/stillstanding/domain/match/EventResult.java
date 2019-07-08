package com.zaca.stillstanding.domain.match;

import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.team.Team;

class EventResult{
	private final EventResultType type;
	private final JSONObject data;
	
	public EventResult(EventResultType type, JSONObject data) {
		this.type = type;
		this.data = data;
	}
	
	public static EventResult getTypeSwitchTeam(boolean lastCorrect, Team lastTeam, Team currentTeam) {
		JSONObject data = new JSONObject();
		data.put("last_correct", lastCorrect);
		data.put("last_team", lastTeam.getMatchData());
		data.put("current_team", currentTeam.getMatchData());
		return new EventResult(EventResultType.SWITCH_TEAM, data);
	}
}