package hundun.quizgame.server;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hundun.quizgame.core.context.QuizCoreContext;
import hundun.quizgame.core.service.QuestionLoaderService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/07/17
 */
@Slf4j
@Configuration
public class QuizCoreLoader {
    
    @PostConstruct
    public void post() {
        File PACKAGES_FOLDER = new File("../data/packages");
        File RESOURCE_ICON_FOLDER = new File("../data/pictures");
        QuizCoreContext.getInstance().questionLoaderService.lateInitFolder(PACKAGES_FOLDER, RESOURCE_ICON_FOLDER);
    }


}
