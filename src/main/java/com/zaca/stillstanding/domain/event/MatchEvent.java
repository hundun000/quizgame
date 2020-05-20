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
	
	
	
	public static boolean isTypeInCollection(Collection<MatchEvent> events, EventType type) {
        for (MatchEvent event : events) {
            if (event.type == type) {
                return true;
            }
        }
        return false;
    }
	
	public static MatchEvent getTypeStartTeam(Team team, int currentScore, HealthType healthType, int currentHealth, Map<String, Integer> skillRemainTimes) {
		JSONObject data = new JSONObject();
		data.put("team_match_data", team.getMatchData());
		data.put("current_score", currentScore);
        data.put("health_type_code", healthType.getCode());
        data.put("current_health", currentHealth);
		return new MatchEvent(EventType.START_TEAM, data);
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
	
	/**
	 * use type teamHealth
	 * @param team
	 * @return
	 */
	@Deprecated
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

	public static MatchEvent getTypeSkillSuccess(String teamName, String roleName, BaseSkill skill, int skillRemainTime) {
        JSONObject data = new JSONObject();
        data.put("team_name", teamName);
        data.put("role_name", roleName);
        data.put(KEY_SKILL_NAME, skill.getName());
        data.put("static_data", skill.getFrontendData());
        data.put("skill_remain_time", skillRemainTime);
        return new MatchEvent(EventType.SKILL_SUCCESS, data);
    }
	
	public static MatchEvent getTypeSkillUseOut(String teamName, BaseSkill skill) {
        JSONObject data = new JSONObject();
        data.put("team_name", teamName);
        data.put("skill_name", skill.getName());
        return new MatchEvent(EventType.SKILL_USE_OUT, data);
    }
	
	
	public JSONObject getPayload() {
        return payload;
    }
	
	public EventType getType() {
        return type;
    }



    public static MatchEvent getTypeAnswerResult(AnswerType answerType, int addScore, int currentScore, HealthType healthType, int currentHealth) {
        JSONObject data = new JSONObject();
        data.put("result", answerType);
        data.put("add_score", addScore);
        data.put("current_score", currentScore);
        data.put("health_type_code", healthType.getCode());
        data.put("current_health", currentHealth);
        return new MatchEvent(EventType.ANSWER_RESULT, data);
    }

}