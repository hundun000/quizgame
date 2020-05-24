package com.zaca.stillstanding.domain.buff;
/**
 * @author hundun
 * Created on 2020/05/20
 */
public class ScoreComboBuffEffect implements IBuffEffect {
    
    public int getScoreOffset(int buffDuration) {
        return buffDuration > 0 ? buffDuration - 1 : 0;
    }

}
