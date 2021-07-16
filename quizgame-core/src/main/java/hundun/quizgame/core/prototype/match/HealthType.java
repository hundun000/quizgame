package hundun.quizgame.core.prototype.match;
/**
 * @author hundun
 * Created on 2020/01/11
 */
public enum HealthType {
    
    /**
     * 生命值即为剩余可连续答错数
     */
    CONSECUTIVE_WRONG_AT_LEAST(0), 
    /**
     * 生命值即为已答题目总数
     */
    SUM(1),
    /**
     * 无尽
     */
    ENDLESS(2),
    ;
    
    
    private final int code;
    
    public int getCode() {
        return code;
    }
    
    private HealthType(int code) {
        this.code = code;
    }

}
