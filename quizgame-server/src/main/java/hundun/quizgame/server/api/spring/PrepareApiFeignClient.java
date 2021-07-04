package hundun.quizgame.server.api.spring;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import feign.Response;
import hundun.quizgame.server.api.QuizgameApiFeignConfiguration;






@FeignClient(
        name = "prepareApiFeignClient",
        url = "http://localhost:10100/api/prepare",
        configuration = QuizgameApiFeignConfiguration.class
)
@Component
public interface PrepareApiFeignClient extends IPrepareApi {
    
    
}
