package com.zaca.stillstanding.service;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.zaca.stillstanding.domain.question.Question;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.QuestionFormatException;
import com.zaca.stillstanding.tool.QuestionTool;

@Service
public class QuestionService {
	
	
	
	private List<Question> questions;
	private List<Question> dirtyQuestions;
	private Random random = new Random();
	
	public void initQuestions() {
		try {
			this.questions = new LinkedList<>(QuestionTool.LoadAllQuestions());
			Collections.shuffle(questions);
			this.dirtyQuestions = new LinkedList<>();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (QuestionFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Question getNewQuestionForTeam(Team team) {
		int index;
		boolean hitPickUp = random.nextDouble() < team.getHitPickUpRate();
		if (hitPickUp) {
			index = getFirstPickUpQuestionIndex(team);
			team.resetHitPickUpRate();
		} else {
			index = getFirstNotBanQuestionIndex(team);
			team.increaseHitPickUpRate();
		}
		Question question = questions.remove(index);
		dirtyQuestions.add(question);
		return question;
	}
	
	
	private int getFirstPickUpQuestionIndex(Team team) {
		Question question = null;
		int i = 0;	
		while (question == null && i < questions.size()) {
			question = questions.get(i++);
			
			if (!team.isPickUpAndNotBan(question.getTags())) {
				question = null;
			}
		}
		return i;
	}
	
	private int getFirstNotBanQuestionIndex(Team team) {
		Question question = null;
		int i = 0;	
		while (question == null && i < questions.size()) {
			question = questions.get(i++);
			
			if (!team.isNotBan(question.getTags())) {
				question = null;
			}
		}
		return i;
	}

}
