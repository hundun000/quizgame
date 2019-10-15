package com.zaca.stillstanding.domain.event;

import java.util.Collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.skill.BaseSkill;
import com.zaca.stillstanding.domain.team.Team;

public class MatchEvent{
	private final EventType type;
	private final JSONObject data;
	
	public MatchEvent(EventType type, JSONObject data) {
		this.type = type;
		this.data = data;
	}
	
	
	
	public static boolean isTypeInCollection(Collection<MatchEvent> events, EventType type) {
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
		return new MatchEvent(EventType.SWITCH_TEAM, data);
	}
	
	public static MatchEvent getTypeSwitchQuestion(int time) {
        JSONObject data = new JSONObject();
        data.put("time", time);
        return new MatchEvent(EventType.SWITCH_QUESTION, data);
    }
	
	public static MatchEvent getTypeTeamDie(Team team) {
        JSONObject data = new JSONObject();
        data.put("team", team.getMatchData());
        return new MatchEvent(EventType.TEAM_DIE, data);
    }
	
	public static MatchEvent getTypeFinish() {
        JSONObject data = new JSONObject();
        return new MatchEvent(EventType.FINISH, data);
    }
	
	public static final String KEY_SKILL_NAME = "skill_name";
	public static MatchEvent getTypeSkillSuccess(Team team, BaseSkill skill, JSON skillEffect) {
        JSONObject data = new JSONObject();
        data.put("team", team.getMatchData());
        data.put(KEY_SKILL_NAME, skill.getName());
        if (skillEffect != null) {
            data.put("skill_effect", skillEffect);
        }
        if (skill.getStaticData() != null) {
            data.put("static_data", skill.getStaticData());
        }
        return new MatchEvent(EventType.SKILL_SUCCESS, data);
    }
	
	public static MatchEvent getTypeSkillUseOut(Team team, BaseSkill skill) {
        JSONObject data = new JSONObject();
        data.put("team", team.getMatchData());
        data.put("skill_name", skill.getName());
        return new MatchEvent(EventType.SKILL_USE_OUT, data);
    }
	
	
	public JSONObject getData() {
        return data;
    }
	public EventType getType() {
        return type;
    }
}