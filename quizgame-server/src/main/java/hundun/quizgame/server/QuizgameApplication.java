package hundun.quizgame.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication(scanBasePackages = {"hundun.quizgame"})
public class QuizgameApplication {
 
	public static void main(String[] args) {
		SpringApplication.run(QuizgameApplication.class, args);
	}
	
	

}
