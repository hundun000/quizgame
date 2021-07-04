package hundun.quizgame.core.gui;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hundun.quizgame.core.dto.match.MatchConfigDTO;
import hundun.quizgame.core.dto.match.MatchSituationDTO;
import hundun.quizgame.core.service.GameService;

/**
 * @author hundun
 * Created on 2019/09/12
 */
public class GUILoader {

    
    boolean usingSmallFrame = true;
    int SMALL_WIDTH = 460;
    int BIG_WIDTH = 850;
    
    GameService gameService;
    
    MyFrame frame;
    
    public GUILoader(GameService gameService) {
        this.gameService = gameService;
                        
        frame = new MyFrame(gameService);
        
        
        if (usingSmallFrame) {
            frame.setBounds(frame.getX(), frame.getY(), SMALL_WIDTH, frame.getHeight());
        } else {
            frame.setBounds(frame.getX(), frame.getY(), BIG_WIDTH, frame.getHeight());
        }

    }

}
