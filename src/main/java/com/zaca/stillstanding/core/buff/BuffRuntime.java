package com.zaca.stillstanding.core.buff;

import com.zaca.stillstanding.dto.buff.BuffRuntimeDTO;

/**
 * @author hundun
 * Created on 2020/05/20
 */
public class BuffRuntime {
    
    private final BuffPrototype prototype;
    private int duration;
    
    public BuffRuntime(BuffPrototype model, int duration) {
        this.prototype = model;
        this.duration = duration;
    }
    
    public void minusOneDurationAndCheckMaxDuration() {
        duration--;
        if (duration > prototype.getMaxDuration()) {
            duration = prototype.getMaxDuration();
        }
    }
    
    public void clearDuration() {
        duration = 0;
    }
    
    public void addDuration(int plus) {
        duration += plus;
    }
    
    public BuffPrototype getPrototype() {
        return prototype;
    }
    
    public int getDuration() {
        return duration;
    }

    public BuffRuntimeDTO toRunTimeBuffDTO() {
        BuffRuntimeDTO dto = new BuffRuntimeDTO();
        dto.setName(this.prototype.getName());
        dto.setDescription(this.prototype.getDescription());
        dto.setDuration(duration);
        return dto;
    }

}
