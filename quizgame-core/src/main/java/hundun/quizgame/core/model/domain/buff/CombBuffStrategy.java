package hundun.quizgame.core.model.domain.buff;
/**
 * @author hundun
 * Created on 2020/05/20
 */
public class CombBuffStrategy implements IBuffStrategy {
    
    public CombBuffStrategy() {
        
    }

    @Override
    public int modifyScore(int baseScoreAddition, int buffDuration) {
        return buffDuration > 0 ? baseScoreAddition + buffDuration : baseScoreAddition;
    }

}
