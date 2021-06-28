package com.zaca.stillstanding.gui;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.zaca.stillstanding.dto.match.MatchConfigDTO;
import com.zaca.stillstanding.dto.match.MatchSituationDTO;
import com.zaca.stillstanding.service.GameService;

/**
 * @author hundun
 * Created on 2019/09/12
 */
@Component
public class GUILoader implements CommandLineRunner{
    @Value("${using-GUI}")
    boolean usingGUI;
    
    boolean usingSmallFrame = true;
    int SMALL_WIDTH = 460;
    int BIG_WIDTH = 850;
    
    
    
    
    @Autowired
    GameService gameService;
    
    MyFrame frame;
    
    @Override
    public void run(String... args) throws Exception {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
                    if (usingGUI) {
                        
                        frame = new MyFrame(gameService);
                        
                        
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
