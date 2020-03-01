package com.zaca.stillstanding.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zaca.stillstanding.domain.question.Question;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.QuestionFormatException;
import com.zaca.stillstanding.tool.QuestionTool;

@Service
public class QuestionService {
	
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);
	
	private List<Question> questions;
	private List<Question> dirtyQuestions;
	private Set<String> tags;
	private Random hitRandom = new Random(1);
	private Random shuffleRandom = new Random(1);
	
	public void initQuestions(String questionPackageName) {
		try {
			this.questions = QuestionTool.LoadAllQuestions(questionPackageName);
			Collections.shuffle(questions, shuffleRandom);
		} catch (IOException e) {
			e.printStackTrace();
			this.questions = new ArrayList<>();
		} catch (QuestionFormatException e) {
			e.printStackTrace();
			this.questions = new ArrayList<>();
		}
		
		this.dirtyQuestions = new LinkedList<>();
		this.tags = new HashSet<>();
		
		questions.forEach(question -> tags.addAll(question.getTags()));
	}
	
	public Question getNewQuestionForTeam(Team team) {
		
	    int index;
		boolean hitPick = hitRandom.nextDouble() < team.getHitPickRate();
		if (hitPick) {
			index = getFirstPickQuestionIndex(team);
			team.resetHitPickRate();
			logger.debug("FirstPickQuestionIndex = {}", index);
		} else {
			index = getFirstNotBanQuestionIndex(team);
			team.increaseHitPickRate();
			logger.debug("FirstNotBanQuestionIndex = {}", index);
		}
		if (index < 0) {
		    index = 0;
		    logger.warn("没有合适的题目。hitPick={}, team={}", hitPick, team.getAllData().toString());
		}
		Question question = questions.remove(index);
		dirtyQuestions.add(question);
		return question;
	}
	
	
	private int getFirstPickQuestionIndex(Team team) {
		Question question = null;
		int i = 0;	
		while (question == null && i < questions.size()) {
			question = questions.get(i);
			
			if (!team.isPickAndNotBan(question.getTags())) {
				question = null;
				i++;
			}
		}
		if (i == questions.size()) {
            i = -1;
        }
		return i;
	}
	
	private int getFirstNotBanQuestionIndex(Team team) {
		Question question = null;
		int i = 0;	
		while (question == null && i < questions.size()) {
			question = questions.get(i);
			
			if (!team.isNotBan(question.getTags())) {
				question = null;
				i++;
			}
		}
		if (i == questions.size()) {
		    i = -1;
		}
		return i;
	}
	
	public Set<String> getTags() {
        return tags;
    }

}
