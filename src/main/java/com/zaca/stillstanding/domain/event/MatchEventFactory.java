package com.zaca.stillstanding.domain.event;

import java.util.Collection;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.question.AnswerType;
import com.zaca.stillstanding.domain.skill.BaseSkill;
import com.zaca.stillstanding.domain.team.HealthType;
import com.zaca.stillstanding.domain.team.Team;

/**
 * @author hundun
 * Created on 2020/05/23
 */
public class MatchEventFactory {
    
    public static boolean isTypeInCollection(Collection<MatchEvent> events, EventType type) {
        for (MatchEvent event : events) {
            if (event.getType() == type) {
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
    
    
    public static MatchEvent getTypeFinish(Map<String, Integer> scores) {
        JSONObject data = new JSONObject();
        data.put("scores", scores);
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
