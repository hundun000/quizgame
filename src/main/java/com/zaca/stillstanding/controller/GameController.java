package com.zaca.stillstanding.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.match.PreMatch;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.StillStandingException;

/**
 *
 * @author hundun
 * Created on 2019/03/19
 */
@RestController
@RequestMapping("/api/game")
public class GameController {
    
    @Autowired
    PreMatch preMatch;
    
    @RequestMapping(value="", method=RequestMethod.GET)
    public Object listTeams() {
        MatchEvent event;
        try {
            event = preMatch.start();
        } catch (StillStandingException e) {
            e.printStackTrace();
            return null;
        }
        return event;
    }
    
    
    

}
