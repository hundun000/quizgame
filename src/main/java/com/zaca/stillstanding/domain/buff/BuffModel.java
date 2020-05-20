package com.zaca.stillstanding.domain.buff;

import java.util.LinkedList;
import java.util.List;

/**
 * 技能的持续性/延迟性效果。debuff也视为一种Buff。本类是Buff的享元。
 * @author hundun
 * Created on 2020/05/20
 */
public class BuffModel {
    private String name;
    private String description;
    List<IBuffEffect> buffEffects = new LinkedList<>();
    private int maxDuration;
    
    public BuffModel(String name, String description, int maxDuration) {
        this.name = name;
        this.description = description;
        this.maxDuration = maxDuration;
    }
    
    public BuffModel addBuffEffect(IBuffEffect buffEffect) {
        buffEffects.add(buffEffect);
        return this;
    }
    
    public List<IBuffEffect> getBuffEffects() {
        return buffEffects;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getMaxDuration() {
        return maxDuration;
    }

}
