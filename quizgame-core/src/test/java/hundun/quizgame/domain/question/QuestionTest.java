package hundun.quizgame.domain.question;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import hundun.quizgame.core.exception.ConflictException;
import hundun.quizgame.core.exception.NotFoundException;
import hundun.quizgame.core.model.question.TagManager;
import hundun.quizgame.core.model.team.TeamPrototype;
import hundun.quizgame.core.model.team.TeamRuntime;
import hundun.quizgame.core.service.TeamService;

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
		TeamPrototype teamRuntime = teamService.getTeam(testTeamName);
		
		tags = new HashSet<>();
		tags.add("动画");
		tags.add("fz");
		assertEquals(false, teamRuntime.isNotBan((Set<String>) tags));
	
		tags.remove("动画");
		assertEquals(true, teamRuntime.isNotBan((Set<String>) tags));
		
	}

}
