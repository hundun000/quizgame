package com.zaca.stillstanding.domain.match;

import java.awt.Event;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.question.Question;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.TeamService;

@Component
public class CoupleMatch {
	
	private List<Team> teams = new ArrayList<>();
	private Team currentTeam;
	private int currentTeamIndex;
	private Question currentQuestion;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
    private TeamService teamService;
	
	public void init(String... teamNames) throws NotFoundException {
		this.teams.clear();
		for (String teamName : teamNames) {
		    teams.add(teamService.getTeam(teamName));
		}
		this.currentTeam = null;
	}
	
	public void start() {
        
    }
	
	public EventResult teamAnswer(String answer) {
		boolean correct = currentQuestion.isCorrect(answer);
		if (correct) {
			currentTeam.addScore(1);
		}
		Team lastTeam = currentTeam;
		switchTeamAndNewQuestion();
		return EventResult.getTypeSwitchTeam(correct, lastTeam, currentTeam);
	}
	
	public void teamTimeout() {
		switchTeamAndNewQuestion();
	}
	
	private void switchTeamAndNewQuestion() {
		int nextTeamIndex = currentTeamIndex + 1;
		if (nextTeamIndex == teams.size()) {
			nextTeamIndex = 0;
		}
		
		currentTeam = teams.get(nextTeamIndex);
		currentTeamIndex = nextTeamIndex;
		currentQuestion = questionService.getNewQuestionForTeam(currentTeam);
	}

    public List<Team> getTeams() {
        return teams;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public QuestionService getQuestionService() {
        return questionService;
    }
	
	public int getCurrentTeamIndex() {
        return currentTeamIndex;
    }
	
}
