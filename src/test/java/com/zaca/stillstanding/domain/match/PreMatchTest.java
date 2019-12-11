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
    }
    
    @Test
    public void test() throws StillStandingException {
        MatchEvent skillEvent;
        List<MatchEvent> answerEvents;
        String skillName;
        
        match.start();
        System.out.println(FormatTool.matchToJSON(match));
        
        answerEvents = match.teamAnswer("A");
        System.out.println(FormatTool.eventsToShortJSON(answerEvents));
        assertTrue(match.containsEventByType(EventType.SWITCH_QUESTION));
        
        skillName = "5050";
        skillEvent = match.teamUseSkill(skillName);
        System.out.println(JSON.toJSONString(skillEvent));
        assertEquals(skillName, skillEvent.getPayload().getString(MatchEvent.KEY_SKILL_NAME));
        assertEquals(2, skillEvent.getPayload().getJSONObject("static_data").getIntValue("x"));
        
        skillName = "求助";
        skillEvent = match.teamUseSkill(skillName);
        System.out.println(JSON.toJSONString(skillEvent));
        assertEquals(skillName, skillEvent.getPayload().getString(MatchEvent.KEY_SKILL_NAME));
        assertEquals(30, skillEvent.getPayload().getJSONObject("static_data").getIntValue("x"));
        
        skillName = "加时";
        skillEvent = match.teamUseSkill(skillName);
        System.out.println(JSON.toJSONString(skillEvent));
        assertEquals(skillName, skillEvent.getPayload().getString(MatchEvent.KEY_SKILL_NAME));
        assertEquals(15, skillEvent.getPayload().getJSONObject("static_data").getIntValue("x"));
        
        skillName = "跳过";
        skillEvent = match.teamUseSkill(skillName);
        System.out.println(JSON.toJSONString(skillEvent));
        assertEquals(skillName, skillEvent.getPayload().getString(MatchEvent.KEY_SKILL_NAME));
        assertTrue(match.containsEventByType(EventType.SWITCH_QUESTION));
        
        skillEvent = match.teamUseSkill("5050");
        System.out.println(JSON.toJSONString(skillEvent));
        assertEquals("5050", skillEvent.getPayload().getString(MatchEvent.KEY_SKILL_NAME));
        
        skillEvent = match.teamUseSkill("5050");
        System.out.println(JSON.toJSONString(skillEvent));
        assertEquals(EventType.SKILL_USE_OUT, skillEvent.getType());
        
        answerEvents = match.teamAnswer("A");
        System.out.println(FormatTool.eventsToShortJSON(answerEvents));
        assertTrue(match.containsEventByType(EventType.SWITCH_QUESTION));
        
        answerEvents = match.teamAnswer("B");
        System.out.println(FormatTool.eventsToShortJSON(answerEvents));
        assertTrue(match.containsEventByType(EventType.SWITCH_QUESTION));
        
        answerEvents = match.teamAnswer("C");
        System.out.println(FormatTool.eventsToShortJSON(answerEvents));
        assertTrue(match.containsEventByType(EventType.SWITCH_QUESTION));
        
        answerEvents = match.teamAnswer("D");
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
