package com.zaca.stillstanding.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zaca.stillstanding.core.SessionDataPackage;
import com.zaca.stillstanding.core.question.Question;
import com.zaca.stillstanding.core.team.Team;
import com.zaca.stillstanding.dto.question.ResourceType;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.tool.QuestionTool;

@Service
public class QuestionService {
    
    @Autowired
    SessionService sessionService;
	
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);
	

	private Random hitRandom = new Random(1);
	private Random insertQuestionRandom = new Random(1);
	
	
	
	Map<String, List<Question>> questionPackages = new HashMap<>();
	Map<String, Question> questionPool = new HashMap<>();

	
	public Question getNewQuestionForTeam(String sessionId, Team team, boolean removeToDirty) throws StillStandingException {
	    SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
  
	    Question question;
		boolean hitPick = hitRandom.nextDouble() < team.getHitPickRate();
		if (hitPick) {
		    question = getFirstPickQuestionIndex(sessionId, team);
			team.resetHitPickRate();
			logger.debug("FirstPickQuestionIndex");
		} else {
		    question = getFirstNotBanQuestionIndex(sessionId, team);
			team.increaseHitPickRate();
			logger.debug("FirstNotBanQuestionIndex");
		}
		if (question == null) {
		    question = getFirstQuestionIgnorePickBan(sessionId, team);
		}
		logger.info("questionDTO = {}", question.toQuestionDTO());
		
		sessionDataPackage.getQuestionIds().remove(question.getId());
		if (removeToDirty) {
		    sessionDataPackage.getDirtyQuestionIds().add(question);
		} else {
		    int insertIndex = Math.min(sessionDataPackage.getQuestionIds().size(), sessionDataPackage.getQuestionIds().size()/2 + insertQuestionRandom.nextInt(sessionDataPackage.getQuestionIds().size()/2));
		    sessionDataPackage.getQuestionIds().add(insertIndex, question.getId());
		}
		
		return question;
	}
	
	
	private Question getFirstQuestionIgnorePickBan(String sessionId, Team team) throws StillStandingException {
	    SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        Question question = null;
        int i = 0;  
        String questionId = sessionDataPackage.getQuestionIds().get(i);
        question = questionPool.get(questionId); 
           
        return question;
    }


    private Question getFirstPickQuestionIndex(String sessionId, Team team) throws StillStandingException {
	    SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
		Question question = null;
		int i = 0;	
		while (question == null && i < sessionDataPackage.getQuestionIds().size()) {
		    String questionId = sessionDataPackage.getQuestionIds().get(i);
            question = questionPool.get(questionId); 
			
			if (!team.isPickAndNotBan(question.getTags())) {
				question = null;
				i++;
				continue;
			}
			if (!sessionDataPackage.isAllowImageResource() && question.getResource().getType() == ResourceType.IMAGE) {
                question = null;
                i++;
                continue;
            }
            if (!sessionDataPackage.isAllowVoiceResource() && question.getResource().getType() == ResourceType.VOICE) {
                question = null;
                i++;
                continue;
            }
		}
		return question;
	}
	
	private Question getFirstNotBanQuestionIndex(String sessionId, Team team) throws StillStandingException {
	    SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
		Question question = null;
		int i = 0;	
		while (question == null && i < sessionDataPackage.getQuestionIds().size()) {
			String questionId = sessionDataPackage.getQuestionIds().get(i);
			question = questionPool.get(questionId); 
			
			if (!team.isNotBan(question.getTags())) {
				question = null;
				i++;
				continue;
			}
			if (!sessionDataPackage.isAllowImageResource() && question.getResource().getType() == ResourceType.IMAGE) {
                question = null;
                i++;
                continue;
            }
			if (!sessionDataPackage.isAllowVoiceResource() && question.getResource().getType() == ResourceType.VOICE) {
                question = null;
                i++;
                continue;
            }
		}
		return question;
	}
	
	

    public List<Question> getQuestions(String questionPackageName) throws StillStandingException {
        if (!questionPackages.containsKey(questionPackageName)) {
            List<Question> questions = QuestionTool.LoadAllQuestions(questionPackageName);
            questionPackages.put(questionPackageName, questions);
            questions.forEach(item -> questionPool.put(item.getId(), item));
        }
        return questionPackages.get(questionPackageName);
    }
    
//    public Set<String> getTags(String sessionId) {
//        SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
//        return sessionDataPackage.getTags();
//    }

}
