package com.zaca.stillstanding.domain.event;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zaca.stillstanding.domain.dto.AnswerType;
import com.zaca.stillstanding.domain.dto.EventType;
import com.zaca.stillstanding.domain.dto.MatchEvent;
import com.zaca.stillstanding.domain.dto.TeamDTO;
import com.zaca.stillstanding.domain.dto.event.AnswerResultEvent;
import com.zaca.stillstanding.domain.dto.event.FinishEvent;
import com.zaca.stillstanding.domain.dto.event.SkillResultEvent;
import com.zaca.stillstanding.domain.dto.event.StartMatchEvent;
import com.zaca.stillstanding.domain.dto.event.StubEvent;
import com.zaca.stillstanding.domain.dto.event.SwitchTeamEvent;
import com.zaca.stillstanding.domain.skill.BaseSkill;
import com.zaca.stillstanding.domain.team.HealthType;
import com.zaca.stillstanding.domain.team.Team;

/**
 * @author hundun
 * Created on 2020/05/23
 */
public class MatchEventFactory {
    
    static ObjectMapper mapper = new ObjectMapper(); 
    
    public static boolean isTypeInCollection(Collection<MatchEvent> events, EventType type) {
        for (MatchEvent event : events) {
            if (event.getType() == type) {
                return true;
            }
        }
        return false;
    }
    
    public static StubEvent getTypeStartTeam(Team team, int currentScore, HealthType healthType, int currentHealth, Map<String, Integer> skillRemainTimes) {
//        ObjectNode data = mapper.createObjectNode();
//        data.put("team_match_data", team.toMatchDataPayload().toJSONString());
//        data.put("current_score", currentScore);
//        data.put("health_type_code", healthType.getCode());
//        data.put("current_health", currentHealth);
        StubEvent event = new StubEvent();
        event.setType(EventType.START_TEAM);
        return event;
    }
    
    public static SwitchTeamEvent getTypeSwitchTeam(Team lastTeam, Team currentTeam) {
//        ObjectNode data = mapper.createObjectNode();
//        data.put("last_team", lastTeam.toMatchDataPayload().toJSONString());
//        data.put("current_team", currentTeam.toMatchDataPayload().toJSONString());
        SwitchTeamEvent event = new SwitchTeamEvent();
        event.setType(EventType.SWITCH_TEAM);
        return event;
    }
    
    public static StubEvent getTypeSwitchQuestion(int time) {
//        ObjectNode data = mapper.createObjectNode();
//        data.put("time", time);
        StubEvent event = new StubEvent();
        event.setType(EventType.SWITCH_QUESTION);
        return event;
    }
    
    
    public static FinishEvent getTypeFinish(Map<String, Integer> scores) {
//        ObjectNode data = mapper.createObjectNode();
//        data.put("scores", scores.toString());
        FinishEvent event = new FinishEvent();
        event.setType(EventType.FINISH);
        return event;
    }
    
    public static final String KEY_SKILL_NAME = "skill_name";

    public static SkillResultEvent getTypeSkillSuccess(String teamName, String roleName, BaseSkill skill, int skillRemainTime) {
        SkillResultEvent event = new SkillResultEvent();
        event.setType(EventType.SKILL_SUCCESS);
        event.setTeamName(teamName);
        event.setRoleName(roleName);
        event.setSkillName(skill.getName());
        event.setSkillDesc(skill.getDescription());
        event.setSkillRemainTime(skillRemainTime);
        event.setArgs(skill.getFrontendData());
        
        return event;
    }
    
    public static SkillResultEvent getTypeSkillUseOut(String teamName, String roleName, BaseSkill skill) {
        SkillResultEvent event = new SkillResultEvent();
        event.setType(EventType.SKILL_USE_OUT);
        event.setTeamName(teamName);
        event.setRoleName(roleName);
        event.setSkillName(skill.getName());
        event.setSkillDesc(skill.getDescription());
        event.setSkillRemainTime(0);
        event.setArgs(skill.getFrontendData());
        
        return event;
    }
    
    public static AnswerResultEvent getTypeAnswerResult(AnswerType answerType, int addScore, int currentScore, HealthType healthType, int currentHealth) {
        AnswerResultEvent event = new AnswerResultEvent();
        event.setType(EventType.ANSWER_RESULT);
        event.setAddScore(addScore);
        event.setResult(answerType);
        return event;
    }

    public static StartMatchEvent getTypeStartTeam(int currentTeamIndex, List<Team> teams) {
        StartMatchEvent event = new StartMatchEvent();
        event.setType(EventType.START_TEAM);
        return event;
    }

}
