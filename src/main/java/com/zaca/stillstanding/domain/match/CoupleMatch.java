package com.zaca.stillstanding.domain.match;

import java.awt.Event;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.question.Question;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.service.QuestionService;

public class CoupleMatch {
	
	private final List<Team> teams;
	private Team currentTeam;
	private Question currentQuestion;
	
	@Autowired
	private QuestionService questionService;
	
	public CoupleMatch(List<Team> teams) {
		this.teams = teams;
		this.currentTeam = null;
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
		int nextTeamIndex = teams.indexOf(currentTeam) + 1;
		if (nextTeamIndex == teams.size()) {
			nextTeamIndex = 0;
		}
		
		currentTeam = teams.get(nextTeamIndex);
		currentQuestion = questionService.getNewQuestionForTeam(currentTeam);
	}
	
}
