package com.zaca.stillstanding.gui;

import java.awt.EventQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.zaca.stillstanding.domain.match.BaseMatch;
import com.zaca.stillstanding.domain.match.PreMatch;
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
    
    @Autowired
    QuestionService questionService;
    
    @Autowired
    TeamService teamService;
    
    @Autowired
    PreMatch match;
    
    MyFrame frame;
    
    @Override
    public void run(String... args) throws Exception {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
                    if (usingGUI) {
                        frame = new MyFrame(questionService, teamService, match);
                        frame.start();
                    }
                    
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

}
