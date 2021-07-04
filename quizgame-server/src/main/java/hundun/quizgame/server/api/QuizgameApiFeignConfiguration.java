package hundun.quizgame.server.api;



import org.springframework.context.annotation.Bean;

import feign.Logger;

public class QuizgameApiFeignConfiguration {

    @Bean
    Logger.Level level() {
        return Logger.Level.BASIC;
    }
    
}
