package com.zaca.stillstanding.controller;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.service.GameService;
import com.zaca.stillstanding.service.QuestionService;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    
    
    @Autowired
    QuestionService questionService;
    
    @RequestMapping(value="/tags", method=RequestMethod.GET)
    public Object listTeams() {
        logger.info("===== \"\" {} =====");
        Collection<String> tags = questionService.getTags();
        return tags;
    }
	

}
