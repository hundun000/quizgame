package com.zaca.stillstanding.domain.match;

import java.awt.Event;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.question.Question;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.TeamService;

@Component
public abstract class CoupleMatch {
	
	protected List<Team> teams = new ArrayList<>();
	protected Team currentTeam;
	protected int currentTeamIndex;
	protected Question currentQuestion;
	
	@Autowired
	protected QuestionService questionService;
	
	@Autowired
	protected TeamService teamService;
	
	AnswerRecorder recorder = new AnswerRecorder();
	
	List<MatchEvent> events = new ArrayList<>();
	
	public void init(String... teamNames) throws NotFoundException {
		this.teams.clear();
		for (String teamName : teamNames) {
		    teams.add(teamService.getTeam(teamName));
		}
		this.currentTeam = null;
	}
	
	public void start() {
	    currentTeamIndex = teams.size() - 1;
	    switchTeamAndNewQuestion();
    }
	

	public List<MatchEvent> teamAnswer(String answer) {
	    events.clear();
	    boolean correct = currentQuestion.isCorrect(answer);
        // 1. 记录回答
		recorder.addRecord(currentTeam.getName(), answer, currentQuestion.getId(), correct);
		// 2. 结算加分
		addScore(correct);
		// 3.判断队伍死亡
        events.add(checkTeamDieEvent());
		// 4.判断换队
		events.add(checkSwitchTeamEvent());
		// 5.判断比赛结束
		events.add(checkTeamDieEvent());
		// 移除空元素
		events = events.stream().filter(s -> s != null).collect(Collectors.toList());
		return events;
	}
	
	/**
	 * 为刚刚的答题加分。
	 * 可实现为：固定加分；连续答对comb加分...
	 * @param correct 
	 */
	abstract protected void addScore(boolean correct);
	
	/**
	 * 判断队伍是否死亡。
	 * 可实现为：累计答n题死亡；连续答错n题死亡；累计答错n题死亡...
	 * @return
	 */
	abstract protected MatchEvent checkTeamDieEvent();

	
	/**
	 * 判断是否切换队伍。
     * 可实现为：答完一题切换；答错才切换...
	 * @return
	 */
	abstract protected MatchEvent checkSwitchTeamEvent();

	
	protected void switchTeamAndNewQuestion() {
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
