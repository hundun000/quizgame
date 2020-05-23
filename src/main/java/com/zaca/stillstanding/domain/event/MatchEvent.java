package com.zaca.stillstanding.domain.event;

import java.util.Collection;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.question.AnswerType;
import com.zaca.stillstanding.domain.skill.BaseSkill;
import com.zaca.stillstanding.domain.skill.RoleRuntimeData;
import com.zaca.stillstanding.domain.team.HealthType;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.StillStandingException;

public class MatchEvent{
	private final EventType type;
	private final JSONObject payload;
	
	public MatchEvent(EventType type, JSONObject data) {
		this.type = type;
		this.payload = data;
	}
	
	
	
	
	
	
	public JSONObject getPayload() {
        return payload;
    }
	
	public EventType getType() {
        return type;
    }



    

}