package hundun.quizgame.gui;

import java.io.File;

import hundun.quizgame.core.context.QuizCoreContext;
import hundun.quizgame.core.service.GameService;

/**
 * @author hundun
 * Created on 2019/09/12
 */
public class GUIMain {

    public static void main(String[] args) {
        boolean usingSmallFrame = true;
        int SMALL_WIDTH = 460;
        int BIG_WIDTH = 850;
        GameService gameService = QuizCoreContext.getInstance().gameService;
        File PACKAGES_FOLDER = new File("../data/packages");
        File RESOURCE_ICON_FOLDER = new File("../data/pictures");
        QuizCoreContext.getInstance().questionLoaderService.lateInitFolder(PACKAGES_FOLDER, RESOURCE_ICON_FOLDER);
        MyFrame frame = new MyFrame(gameService);
        
        
        if (usingSmallFrame) {
            frame.setBounds(frame.getX(), frame.getY(), SMALL_WIDTH, frame.getHeight());
        } else {
            frame.setBounds(frame.getX(), frame.getY(), BIG_WIDTH, frame.getHeight());
        }
    }


}
