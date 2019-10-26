package com.zaca.stillstanding.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.service.QuestionService;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    
    @Autowired
    QuestionService questionService;
    
    @RequestMapping(value="/tags", method=RequestMethod.GET)
    public Object listTeams() {
        Collection<String> tags = questionService.getTags();
        return tags;
    }
	

}
