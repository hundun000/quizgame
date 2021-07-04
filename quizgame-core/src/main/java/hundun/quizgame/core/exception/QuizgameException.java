package hundun.quizgame.core.exception;


/**
 * @author hundun
 * Created on 2019/10/12
 */
public class QuizgameException extends Exception {
    
    private static final long serialVersionUID = 6572553799656923229L;
    
    private final int code;
    
    public QuizgameException(int code) {
        this.code = code;
    }
    
    public QuizgameException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    
    public int getRetcode() {
        return code;
    }

    
    public int getStatus() {
        return 400;
    }

    
    public String getPayload() {
        return null;
    };
    
    
    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
