package hundun.quizgame.core.exception;

public class NotFoundException extends QuizgameException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -237975911570660889L;
	
	
	public NotFoundException(String type, String key) {
	    super(type + ":" + key + "未找到。", 404);

	}

}
