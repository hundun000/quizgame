package com.zaca.stillstanding.domain.question;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.zaca.stillstanding.core.question.TagManager;
import com.zaca.stillstanding.core.team.Team;
import com.zaca.stillstanding.exception.ConflictException;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.service.TeamService;

public class QuestionTest {
	
	TeamService teamService = new TeamService();
	String  testTeamName = "砍口垒同好组";
	
	@Test
	public void banTagsTest() throws ConflictException, NotFoundException {
		teamService.creatTeam(testTeamName);
		TagManager.addTag("动画");
		
		Collection<String> tags = new ArrayList<>();
		tags.add("动画");
		teamService.setBanTagsForTeam(testTeamName, (List<String>) tags);
		Team team = teamService.getTeam(testTeamName);
		
		tags = new HashSet<>();
		tags.add("动画");
		tags.add("fz");
		assertEquals(false, team.isNotBan((Set<String>) tags));
	
		tags.remove("动画");
		assertEquals(true, team.isNotBan((Set<String>) tags));
		
	}

}
