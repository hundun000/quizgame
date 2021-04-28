package com.zaca.stillstanding.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zaca.stillstanding.domain.question.Question;
import com.zaca.stillstanding.domain.question.ResourceType;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.QuestionFormatException;
import com.zaca.stillstanding.tool.QuestionTool;

@Service
public class QuestionService {
	
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);
	

	private Random hitRandom = new Random(1);
	private Random shuffleRandom = new Random(1);
	
	boolean onlyNoneResource;
	

	
	class QuestionPackage {
	    List<Question> questions;
	    List<Question> dirtyQuestions;
	    Set<String> tags;
	}
	Map<String, QuestionPackage> questionPackages = new HashMap<>();
	
	public void initQuestions(String matchId, String questionPackageName) {
		
	    QuestionPackage questionPackage = new QuestionPackage();
	    
	    try {
	        questionPackage.questions = QuestionTool.LoadAllQuestions(questionPackageName);
			Collections.shuffle(questionPackage.questions, shuffleRandom);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	    questionPackage.dirtyQuestions = new LinkedList<>();
	    questionPackage.tags = new HashSet<>();
		
	    questionPackage.questions.forEach(question -> questionPackage.tags.addAll(question.getTags()));
	
	    questionPackages.put(matchId, questionPackage);
	}
	
	public Question getNewQuestionForTeam(String matchId, Team team, boolean removeToDirty) {
	    QuestionPackage questionPackage = questionPackages.get(matchId);
	    
	    int index;
		boolean hitPick = hitRandom.nextDouble() < team.getHitPickRate();
		if (hitPick) {
			index = getFirstPickQuestionIndex(matchId, team);
			team.resetHitPickRate();
			logger.debug("FirstPickQuestionIndex = {}", index);
		} else {
			index = getFirstNotBanQuestionIndex(matchId, team);
			team.increaseHitPickRate();
			logger.debug("FirstNotBanQuestionIndex = {}", index);
		}
		if (index < 0) {
		    index = 0;
		    logger.warn("没有合适的题目。hitPick={}, team={}", hitPick, team.toAllDataPayload().toString());
		}
		
		Question question = questionPackage.questions.remove(index);
		if (removeToDirty) {
		    questionPackage.dirtyQuestions.add(question);
		} else {
		    int insertIndex = Math.min(questionPackage.questions.size(), questionPackage.questions.size()/2 + shuffleRandom.nextInt(questionPackage.questions.size()/2));
		    questionPackage.questions.add(insertIndex, question);
		}
		
		return question;
	}
	
	
	private int getFirstPickQuestionIndex(String matchId, Team team) {
	    QuestionPackage questionPackage = questionPackages.get(matchId);
		Question question = null;
		int i = 0;	
		while (question == null && i < questionPackage.questions.size()) {
			question = questionPackage.questions.get(i);
			
			if (!team.isPickAndNotBan(question.getTags())) {
				question = null;
				i++;
			}
			if (onlyNoneResource && question.getResource().getType() != ResourceType.NONE) {
			    question = null;
                i++;
			}
		}
		if (i == questionPackage.questions.size()) {
            i = -1;
        }
		return i;
	}
	
	private int getFirstNotBanQuestionIndex(String matchId, Team team) {
	    QuestionPackage questionPackage = questionPackages.get(matchId);
		Question question = null;
		int i = 0;	
		while (question == null && i < questionPackage.questions.size()) {
			question = questionPackage.questions.get(i);
			
			if (!team.isNotBan(question.getTags())) {
				question = null;
				i++;
			}
			if (onlyNoneResource && question.getResource().getType() != ResourceType.NONE) {
                question = null;
                i++;
            }
		}
		if (i == questionPackage.questions.size()) {
		    i = -1;
		}
		return i;
	}
	
	public Set<String> getTags(String matchId) {
	    QuestionPackage questionPackage = questionPackages.get(matchId);
        return questionPackage.tags;
    }

}
