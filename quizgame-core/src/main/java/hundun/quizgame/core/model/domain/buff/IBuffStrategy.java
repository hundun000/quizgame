package hundun.quizgame.core.model.domain.buff;
/**
 * @author hundun
 * Created on 2021/07/17
 */
public interface IBuffStrategy {
    public int modifyScore(int baseScoreAddition, int buffDuration);
}
