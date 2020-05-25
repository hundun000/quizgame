package com.zaca.stillstanding.domain.buff;
/**
 * @author hundun
 * Created on 2020/05/20
 */
public class ScoreScaleBuffEffect extends BuffEffect {
    
    private double rate;
    
    public ScoreScaleBuffEffect(double rate) {
        super(BuffEffectType.SCORE_SCALE);
        this.rate = rate;
    }



    public int getScoreOffset(int score) {
        int scoreOffset = (int) (score * rate);
        if (scoreOffset <= 0) {
            scoreOffset = 1;
        }
        return scoreOffset;
    }

}
