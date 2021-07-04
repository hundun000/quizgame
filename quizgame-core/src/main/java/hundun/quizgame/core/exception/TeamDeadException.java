package hundun.quizgame.core.exception;
/**
 * @author hundun
 * Created on 2019/10/12
 */
public class TeamDeadException extends QuizgameException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 4825087489852106495L;

    public TeamDeadException(String teamName) {
        super(teamName + "已经死了", 1);
    }

}
