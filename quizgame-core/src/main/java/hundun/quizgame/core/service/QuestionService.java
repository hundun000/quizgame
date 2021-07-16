package hundun.quizgame.core.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.SessionDataPackage;
import hundun.quizgame.core.model.domain.Question;
import hundun.quizgame.core.model.domain.TeamModel;
import hundun.quizgame.core.prototype.question.ResourceType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuestionService {
    
    @Autowired
    SessionService sessionService;

    @Autowired
    QuestionLoaderService questionLoaderService;
    
	private Random hitRandom = new Random(1);
	private Random insertQuestionRandom = new Random(1);
	
	
	private Set<String> tags = new HashSet<>();
	
	Map<String, List<Question>> questionPackages = new HashMap<>();
	Map<String, Question> questionPool = new HashMap<>();

	
	public Question getNewQuestionForTeam(String sessionId, TeamModel teamModel, boolean removeToDirty) throws QuizgameException {
	    SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
  
	    Question question;
		boolean hitPick = hitRandom.nextDouble() < teamModel.getHitPickRate();
		if (hitPick) {
		    question = getFirstPickQuestionIndex(sessionId, teamModel);
			teamModel.resetHitPickRate();
			log.debug("FirstPickQuestionIndex");
		} else {
		    question = getFirstNotBanQuestionIndex(sessionId, teamModel);
			teamModel.increaseHitPickRate();
			log.debug("FirstNotBanQuestionIndex");
		}
		if (question == null) {
		    question = getFirstQuestionIgnorePickBan(sessionId, teamModel);
		}
		log.info("questionDTO = {}", question.toQuestionDTO());
		
		sessionDataPackage.getQuestionIds().remove(question.getId());
		if (removeToDirty) {
		    sessionDataPackage.getDirtyQuestionIds().add(question);
		} else {
		    int insertIndex = Math.min(sessionDataPackage.getQuestionIds().size(), sessionDataPackage.getQuestionIds().size()/2 + insertQuestionRandom.nextInt(sessionDataPackage.getQuestionIds().size()/2));
		    sessionDataPackage.getQuestionIds().add(insertIndex, question.getId());
		}
		
		return question;
	}
	
	
	private Question getFirstQuestionIgnorePickBan(String sessionId, TeamModel teamModel) throws QuizgameException {
	    SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
        Question question = null;
        int i = 0;  
        if (sessionDataPackage.getQuestionIds().isEmpty()) {
            throw new QuizgameException("题库已经空了", -1);
        }
        String questionId = sessionDataPackage.getQuestionIds().get(i);
        question = questionPool.get(questionId); 
           
        return question;
    }


    private Question getFirstPickQuestionIndex(String sessionId, TeamModel teamModel) throws QuizgameException {
	    SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
		Question question = null;
		int i = 0;	
		while (question == null && i < sessionDataPackage.getQuestionIds().size()) {
		    String questionId = sessionDataPackage.getQuestionIds().get(i);
            question = questionPool.get(questionId); 
			
			if (!teamModel.isPickAndNotBan(question.getTags())) {
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
	
	private Question getFirstNotBanQuestionIndex(String sessionId, TeamModel teamModel) throws QuizgameException {
	    SessionDataPackage sessionDataPackage = sessionService.getSessionDataPackage(sessionId);
		Question question = null;
		int i = 0;	
		while (question == null && i < sessionDataPackage.getQuestionIds().size()) {
			String questionId = sessionDataPackage.getQuestionIds().get(i);
			question = questionPool.get(questionId); 
			
			if (!teamModel.isNotBan(question.getTags())) {
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
	
	

    public List<Question> getQuestions(String questionPackageName) throws QuizgameException {
        if (!questionPackages.containsKey(questionPackageName)) {
            List<Question> questions = questionLoaderService.loadAllQuestions(questionPackageName);
            questionPackages.put(questionPackageName, questions);
            questions.forEach(item -> questionPool.put(item.getId(), item));
        }
        return questionPackages.get(questionPackageName);
    }
    
    public Set<String> getTags(String questionPackageName) throws QuizgameException {
        List<Question> questions = getQuestions(questionPackageName);
        Set<String> tags = new HashSet<>();
        questions.forEach(question -> tags.addAll(question.getTags()));
        return tags;
    }
    
    public void addTag(String tag) {
        tags.add(tag);
    }
    
    public Set<String> getTags() {
        return tags;
    }
    
    public boolean tagExsist(String tag) {
        return tags.contains(tag);
    }

}
