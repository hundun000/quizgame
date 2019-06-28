package com.zaca.stillstanding.domain.match;

import java.util.List;

import com.zaca.stillstanding.domain.question.Question;
import com.zaca.stillstanding.domain.team.Team;

public class CoupleMatch {
	
	private final List<Team> teams;
	private int currentTeamIndex;
	private Question question;
	
	public CoupleMatch(List<Team> teams) {
		this.teams = teams;
		this.currentTeamIndex = 0;
	}
	
	public void teamAnswer(String answer) {
		Team team = teams.get(currentTeamIndex);
		boolean correct = question.isCorrect(answer);
		if (correct) {
			team.addScore(1);
		}
	}
	
	public void switchTeamAndNewQuestion() {
		currentTeamIndex++;
		if (currentTeamIndex == teams.size()) {
			currentTeamIndex = 0;
		}
		
		Team team = teams.get(currentTeamIndex);
		
		
	}
	
	
}
