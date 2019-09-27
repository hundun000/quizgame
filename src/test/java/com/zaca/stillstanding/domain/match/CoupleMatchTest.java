package com.zaca.stillstanding.domain.match;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.zaca.stillstanding.controller.GameController;
import com.zaca.stillstanding.exception.ConflictException;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.service.GameService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.TeamService;
import com.zaca.stillstanding.tool.FormatTool;
import com.zaca.stillstanding.tool.QuestionTool;

/**
 * @author hundun
 * Created on 2019/09/05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class CoupleMatchTest {
    
    @Autowired
    QuestionService questionService;
    @Autowired
    TeamService teamService;
    
    
    @Autowired
    BaseMatch coupleMatch;
    
    String  teamKancolle = "砍口垒同好组";
    String  teamPCR = "PCR同好组";
    
    @Before
    public void init() throws ConflictException, NotFoundException {
        questionService.initQuestions(QuestionTool.TEST_SMALL_PACKAGE_NAME);
        
        teamService.creatTeam(teamKancolle); 
        List<String> pickTagNames = new ArrayList<>();
        pickTagNames.add("单机游戏");
        teamService.setPickTagsForTeam(teamKancolle, pickTagNames);
        
        List<String> banTagNames = new ArrayList<>();
        banTagNames.add("动画");
        teamService.setBanTagsForTeam(teamKancolle, banTagNames);
        
        teamService.creatTeam(teamPCR); 
        pickTagNames = new ArrayList<>();
        pickTagNames.add("单机游戏");
        teamService.setPickTagsForTeam(teamPCR, pickTagNames);
        
        banTagNames = new ArrayList<>();
        banTagNames.add("动画");
        teamService.setBanTagsForTeam(teamPCR, banTagNames);
        
        coupleMatch.init(teamKancolle, teamPCR);
    }
    
    @Test
    public void test() throws NotFoundException {
        coupleMatch.start();
        System.out.println(FormatTool.coupleMatchTOJSON(coupleMatch));
        
        coupleMatch.teamAnswer("A");
        System.out.println(FormatTool.coupleMatchTOJSON(coupleMatch));
        
        coupleMatch.teamAnswer("A");
        System.out.println(FormatTool.coupleMatchTOJSON(coupleMatch));
    }

}
