package com.zaca.stillstanding.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zaca.stillstanding.core.SessionDataPackage;
import com.zaca.stillstanding.core.question.Question;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.exception.StillStandingException;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Service
public class SessionService {
    
    private static int currentId;
    
    @Autowired
    QuestionService questionService;
    
    Map<String, SessionDataPackage> dataPackages = new HashMap<>();
    
    private Random shuffleRandom = new Random(1);
    
    public SessionDataPackage createSession(String questionPackageName) throws StillStandingException {
        
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
    
    @NotNull
    public SessionDataPackage getSessionDataPackage(String sessionId) throws StillStandingException {
        SessionDataPackage sessionDataPackage = dataPackages.get(sessionId);
        if (sessionDataPackage == null) {
            throw new NotFoundException("sessionDataPackage by sessionId", sessionId);
        }
        return sessionDataPackage;
    }
    
}
