package com.zaca.stillstanding.domain.match;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.zaca.stillstanding.domain.event.EventType;
import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.exception.ConflictException;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.exception.TeamDeadException;
import com.zaca.stillstanding.service.GameService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.TeamService;
import com.zaca.stillstanding.tool.FormatTool;
import com.zaca.stillstanding.tool.QuestionTool;

/**
 * @author hundun
 * Created on 2019/09/07
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PreMatchTest {
    @Autowired
    GameService gameService;
    
    BaseMatch match;
    
    
    @Before
    public void init() throws Exception {
        match = gameService.createMatch();
        match.setTeamsByNames("砍口垒同好组");
        match.start();
        System.out.println(FormatTool.matchToJSON(match));
    }
    
    @Test
    public void test() throws StillStandingException {

        List<MatchEvent> answerEvents;
        String skillName;
        
        match.teamAnswer("A");
        answerEvents = match.getEvents();
        System.out.println(FormatTool.eventsToShortJSON(answerEvents));
        assertTrue(match.containsEventByType(EventType.SWITCH_QUESTION));
        
        skillName = "5050";
        match.teamUseSkill(skillName);
        answerEvents = match.getEvents();
        System.out.println(JSON.toJSONString(answerEvents));
        assertEquals(skillName, answerEvents.get(0).getPayload().getString(MatchEvent.KEY_SKILL_NAME));
        assertEquals(2, answerEvents.get(0).getPayload().getJSONObject("static_data").getIntValue("x"));
        
        skillName = "求助";
        match.teamUseSkill(skillName);
        answerEvents = match.getEvents();
        System.out.println(JSON.toJSONString(answerEvents));
        assertEquals(skillName, answerEvents.get(0).getPayload().getString(MatchEvent.KEY_SKILL_NAME));
        assertEquals(30, answerEvents.get(0).getPayload().getJSONObject("static_data").getIntValue("x"));
        
        skillName = "加时";
        match.teamUseSkill(skillName);
        answerEvents = match.getEvents();
        System.out.println(JSON.toJSONString(answerEvents));
        assertEquals(skillName, answerEvents.get(0).getPayload().getString(MatchEvent.KEY_SKILL_NAME));
        assertEquals(15, answerEvents.get(0).getPayload().getJSONObject("static_data").getIntValue("x"));
        
        skillName = "跳过";
        match.teamUseSkill(skillName);
        answerEvents = match.getEvents();
        System.out.println(JSON.toJSONString(answerEvents));
        assertEquals(skillName, answerEvents.get(0).getPayload().getString(MatchEvent.KEY_SKILL_NAME));
        
        skillName = "5050";
        match.teamUseSkill(skillName);
        answerEvents = match.getEvents();
        System.out.println(JSON.toJSONString(answerEvents));
        assertEquals(skillName, answerEvents.get(0).getPayload().getString(MatchEvent.KEY_SKILL_NAME));
        
        skillName = "5050";
        match.teamUseSkill(skillName);
        answerEvents = match.getEvents();
        System.out.println(JSON.toJSONString(answerEvents));
        assertEquals(EventType.SKILL_USE_OUT, answerEvents.get(0).getType());
        
        match.teamAnswer("A");
        answerEvents = match.getEvents();
        System.out.println(FormatTool.eventsToShortJSON(answerEvents));
        assertTrue(match.containsEventByType(EventType.SWITCH_QUESTION));
        
        match.teamAnswer("B");
        answerEvents = match.getEvents();
        System.out.println(FormatTool.eventsToShortJSON(answerEvents));
        assertTrue(match.containsEventByType(EventType.SWITCH_QUESTION));
        
        match.teamAnswer("C");
        answerEvents = match.getEvents();
        System.out.println(FormatTool.eventsToShortJSON(answerEvents));
        assertTrue(match.containsEventByType(EventType.SWITCH_QUESTION));
        
        match.teamAnswer("D");
        answerEvents = match.getEvents();
        System.out.println(FormatTool.eventsToShortJSON(answerEvents));
        assertTrue(match.containsEventByType(EventType.TEAM_DIE));
        
        try {
            match.teamAnswer("A");
            fail();
        } catch (TeamDeadException e) {
            // pass if has expectant Exception
        }
    }

}
