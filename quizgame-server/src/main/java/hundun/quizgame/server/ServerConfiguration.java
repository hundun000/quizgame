package hundun.quizgame.server;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import hundun.quizgame.core.service.QuestionLoaderService;

/**
 * @author hundun
 * Created on 2021/07/17
 */
@Configuration
public class ServerConfiguration {
    @Autowired
    QuestionLoaderService questionLoaderService;
    
    @PostConstruct
    public void post() {
        
        File PACKAGES_FOLDER = new File("data/packages");
        File RESOURCE_ICON_FOLDER = new File("data/pictures");
        questionLoaderService.lateInitFolder(PACKAGES_FOLDER, RESOURCE_ICON_FOLDER);
    }
}
