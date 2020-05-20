package com.zaca.stillstanding.domain.buff;
/**
 * @author hundun
 * Created on 2020/05/20
 */
public class RunTimeBuff {
    
    private final BuffModel model;
    private int duration;
    
    public RunTimeBuff(BuffModel model, int duration) {
        this.model = model;
        this.duration = duration;
    }
    
    public void minusOneDurationAndCheckMaxDuration() {
        duration--;
        if (duration > model.getMaxDuration()) {
            duration = model.getMaxDuration();
        }
    }
    
    public void clearDuration() {
        duration = 0;
    }
    
    public void addDuration(int plus) {
        duration += plus;
    }
    
    public BuffModel getModel() {
        return model;
    }
    
    public int getDuration() {
        return duration;
    }

}
