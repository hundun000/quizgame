package com.zaca.stillstanding.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author hundun
 * Created on 2019/10/16
 */
@RestController
@RequestMapping("/api/teams")
public class TeamController {
    
    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);
    ObjectMapper objectMapper = new ObjectMapper();
    
    
    
    

}
