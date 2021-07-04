package hundun.quizgame.server.api.spring;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import feign.Response;
import hundun.quizgame.server.api.QuizgameApiFeignConfiguration;






@FeignClient(
        name = "gameApiFeignClient",
        url = "http://localhost:10100/api/game",
        configuration = QuizgameApiFeignConfiguration.class
)
@Component
public interface GameApiFeignClient extends IGameApi {
    
    
    @RequestMapping(
            value = "/pictures", 
            method = RequestMethod.GET
    )
    Response pictures(@RequestParam("id") String imageResourceId);
    
}
