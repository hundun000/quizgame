package com.zaca.stillstanding.gui;

import java.awt.EventQueue;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.zaca.stillstanding.core.match.BaseMatch;
import com.zaca.stillstanding.core.match.strategy.PreStrategy;
import com.zaca.stillstanding.domain.dto.MatchConfigDTO;
import com.zaca.stillstanding.domain.dto.MatchSituationDTO;
import com.zaca.stillstanding.service.GameService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.TeamService;

/**
 * @author hundun
 * Created on 2019/09/12
 */
@Component
public class GUILoader implements CommandLineRunner{
    @Value("${using-GUI}")
    boolean usingGUI;
    
    @Value("${using-small-frame}")
    boolean usingSmallFrame;
    int SMALL_WIDTH = 460;
    int BIG_WIDTH = 723;
    
    
    
    MatchSituationDTO match;
    
    @Autowired
    GameService gameService;
    
    MyFrame frame;
    
    @Override
    public void run(String... args) throws Exception {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
                    if (usingGUI) {
                        MatchConfigDTO matchConfigDTO = new MatchConfigDTO();
                        matchConfigDTO.setTeamNames(Arrays.asList("砍口垒同好组", "方舟同好组"));
                        match = gameService.createPreMatch(matchConfigDTO);
                        
                        frame = new MyFrame(match);
                        frame.start();
                        
                        if (usingSmallFrame) {
                            frame.setBounds(frame.getX(), frame.getY(), SMALL_WIDTH, frame.getHeight());
                        } else {
                            frame.setBounds(frame.getX(), frame.getY(), BIG_WIDTH, frame.getHeight());
                        }
                    }
                    
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

}
