package hundun.quizgame.core.exception;

public class ConflictException extends QuizgameException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4076058696749505391L;
	

	
	public ConflictException(String type, String key) {
		super(type + ":" + key + "已存在。", 1);

	}

}
