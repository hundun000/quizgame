package com.zaca.stillstanding.controller;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;

import com.zaca.stillstanding.domain.ApiResult;
import com.zaca.stillstanding.domain.IApiResult;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.service.GameService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.tool.FileTool;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    
    private static final String RESOURCE_ICON_FOLDER = "./data/pictures/";
    
    @Autowired
    QuestionService questionService;
    
    @RequestMapping(value="/tags", method=RequestMethod.GET)
    public Object listTeams() {
        logger.info("===== /tags =====");
        Collection<String> tags = questionService.getTags();
        return tags;
    }
    
    @CrossOrigin
    @RequestMapping(value = "/pictures",method=RequestMethod.GET)
    public IApiResult loadFile(HttpServletResponse response,
            @RequestParam("id") String id
            ) {
        logger.info("===== /pictures {} =====", id);
        String fileName = id;
        String filePathName = RESOURCE_ICON_FOLDER + id;

        File file = new File(filePathName);
        if (!file.exists()) {
            return new ApiResult("文件名找不到文件！" + file.getAbsolutePath());
        }
        try {
            FileTool.putFileToResponse(response, file, fileName);
        } catch (Exception e) {
            return new ApiResult(new StillStandingException(e.getMessage(), -1));
        }
        return new ApiResult("成功");
    }
	

}
