package com.zaca.stillstanding.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zaca.stillstanding.service.QuestionService;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    
    
    
    @Autowired
    QuestionService questionService;

    
    
	

}
