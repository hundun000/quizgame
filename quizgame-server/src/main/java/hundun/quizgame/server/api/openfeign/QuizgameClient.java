package hundun.quizgame.server.api.openfeign;





import feign.Param;
import feign.RequestLine;
import feign.Response;

public interface QuizgameClient extends QuizgameApi {

    @RequestLine("GET /large/{id}")
    Response pictures(@Param("id") String id);
    
}
