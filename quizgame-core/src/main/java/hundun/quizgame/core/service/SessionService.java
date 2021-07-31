package hundun.quizgame.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import hundun.quizgame.core.context.IQuizCoreComponent;
import hundun.quizgame.core.context.QuizCoreContext;
import hundun.quizgame.core.exception.NotFoundException;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.SessionDataPackage;
import hundun.quizgame.core.model.domain.Question;

/**
 * @author hundun
 * Created on 2021/05/10
 */
public class SessionService implements IQuizCoreComponent {
    
    private static int currentId;
    
    QuestionService questionService;
    
    Map<String, SessionDataPackage> dataPackages = new HashMap<>();
    
    private Random shuffleRandom = new Random(System.currentTimeMillis());
    
    @Override
    public void wire(QuizCoreContext quizCoreContext) {
        this.questionService = quizCoreContext.questionService;
    }
    
    public SessionDataPackage createSession(String questionPackageName) throws QuizgameException {
        
        SessionDataPackage sessionDataPackage = new SessionDataPackage();
        

        List<Question> questions = questionService.getQuestions(questionPackageName);
        List<String> questionIds = new ArrayList<>(questions.size());
        Set<String> tags = new HashSet<>();
        for (Question question : questions) {
            questionIds.add(question.getId());
            tags.addAll(question.getTags());
        }
        Collections.shuffle(questionIds, shuffleRandom);

        sessionDataPackage.setSessionId(String.valueOf(currentId++));
        sessionDataPackage.setQuestionIds(questionIds);
        sessionDataPackage.setDirtyQuestionIds(new LinkedList<>());
        sessionDataPackage.setTags(tags);
        
        dataPackages.put(sessionDataPackage.getSessionId(), sessionDataPackage);
        
        return sessionDataPackage;
    }
    
    public SessionDataPackage getSessionDataPackage(String sessionId) throws QuizgameException {
        SessionDataPackage sessionDataPackage = dataPackages.get(sessionId);
        if (sessionDataPackage == null) {
            throw new NotFoundException("sessionDataPackage by sessionId", sessionId);
        }
        return sessionDataPackage;
    }
    
}
