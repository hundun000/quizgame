package com.zaca.stillstanding.core.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaca.stillstanding.core.skill.SkillConstData;
import com.zaca.stillstanding.core.team.HealthType;
import com.zaca.stillstanding.core.team.Team;
import com.zaca.stillstanding.dto.event.AnswerResultEvent;
import com.zaca.stillstanding.dto.event.EventType;
import com.zaca.stillstanding.dto.event.FinishEvent;
import com.zaca.stillstanding.dto.event.MatchEvent;
import com.zaca.stillstanding.dto.event.SkillResultEvent;
import com.zaca.stillstanding.dto.event.StartMatchEvent;
import com.zaca.stillstanding.dto.event.SwitchQuestionEvent;
import com.zaca.stillstanding.dto.event.SwitchTeamEvent;
import com.zaca.stillstanding.dto.match.AnswerType;
import com.zaca.stillstanding.dto.role.RoleConstInfoDTO;
import com.zaca.stillstanding.dto.team.TeamConstInfoDTO;

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
    
    public static StartMatchEvent getTypeStartMatch(List<Team> teams){
        StartMatchEvent event = new StartMatchEvent();
        event.setType(EventType.START_MATCH);
        List<TeamConstInfoDTO> teamConstInfoDTOs = new ArrayList<>();
        List<RoleConstInfoDTO> roleConstInfoDTOs = new ArrayList<>();
        for (Team team : teams) {
            teamConstInfoDTOs.add(team.toTeamDTO());
            if (team.getRole() != null) {
                roleConstInfoDTOs.add(team.getRole().toRoleConstInfoDTO());
            }
        }
        event.setTeamConstInfos(teamConstInfoDTOs);
        event.setRoleConstInfos(roleConstInfoDTOs);
        return event;
    }
    
    public static SwitchTeamEvent getTypeSwitchTeam(Team lastTeam, Team currentTeam) {
        SwitchTeamEvent event = new SwitchTeamEvent();
        event.setType(EventType.SWITCH_TEAM);
        event.setFromTeamName(lastTeam.getName());
        event.setToTeamName(currentTeam.getName());
        return event;
    }
    
    public static SwitchQuestionEvent getTypeSwitchQuestion(int time) {
        SwitchQuestionEvent event = new SwitchQuestionEvent();
        event.setType(EventType.SWITCH_QUESTION);
        event.setTime(time);
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

    public static SkillResultEvent getTypeSkillSuccess(String teamName, String roleName, SkillConstData skill, int skillRemainTime) {
        SkillResultEvent event = new SkillResultEvent();
        event.setType(EventType.SKILL_SUCCESS);
        event.setTeamName(teamName);
        event.setRoleName(roleName);
        event.setSkillName(skill.getName());
        event.setSkillDesc(skill.getDescription());
        event.setSkillRemainTime(skillRemainTime);
        event.setArgs(skill.getEventArgs());
        
        return event;
    }
    
    public static SkillResultEvent getTypeSkillUseOut(String teamName, String roleName, SkillConstData skill) {
        SkillResultEvent event = new SkillResultEvent();
        event.setType(EventType.SKILL_USE_OUT);
        event.setTeamName(teamName);
        event.setRoleName(roleName);
        event.setSkillName(skill.getName());
        event.setSkillDesc(skill.getDescription());
        event.setSkillRemainTime(0);
        event.setArgs(skill.getEventArgs());
        
        return event;
    }
    
    public static AnswerResultEvent getTypeAnswerResult(AnswerType answerType, int addScore, String addScoreTeamName) {
        AnswerResultEvent event = new AnswerResultEvent();
        event.setType(EventType.ANSWER_RESULT);
        event.setAddScore(addScore);
        event.setAddScore(addScore);
        event.setAddScoreTeamName(addScoreTeamName);
        event.setResult(answerType);
        return event;
    }

}
