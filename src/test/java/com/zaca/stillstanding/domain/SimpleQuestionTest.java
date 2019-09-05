package com.zaca.stillstanding.domain;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.bind.annotation.InitBinder;

import com.zaca.stillstanding.domain.question.Question;
import com.zaca.stillstanding.domain.question.TagManager;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.ConflictException;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.exception.QuestionFormatException;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.TeamService;
import com.zaca.stillstanding.tool.QuestionTool;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
public class SimpleQuestionTest {
	
	QuestionService questionService = new QuestionService();
	TeamService teamService = new TeamService();
	String  testTeamName = "砍口垒同好组";
	
	@Test
	public void test() throws IOException, QuestionFormatException, ConflictException, NotFoundException {
		
		
		
		questionService.initQuestions(QuestionTool.TEST_PACKAGE_NAME);
		teamService.creatTeam(testTeamName);
		
		List<String> pickUpTagNames = new ArrayList<>();
		pickUpTagNames.add("单机游戏");
		teamService.setPickUpTagsForTeam(testTeamName, pickUpTagNames);
		
		List<String> banTagNames = new ArrayList<>();
		banTagNames.add("动画");
		teamService.setBanTagsForTeam(testTeamName, banTagNames);
		
		Team team = teamService.getTeam(testTeamName);
		for (int i = 1; i < 20; i++) {
			Question question = questionService.getNewQuestionForTeam(team);
			System.out.println("第"+i+"题"+question.getTags()+"："+question.getStem());
		}
	}


}
