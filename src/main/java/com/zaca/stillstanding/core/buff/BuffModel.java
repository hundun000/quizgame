package com.zaca.stillstanding.core.buff;

import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 技能的持续性/延迟性效果。debuff也视为一种Buff。本类是Buff的享元。
 * @author hundun
 * Created on 2020/05/20
 */
public class BuffModel {
    private String name;
    private String description;
    List<BuffEffect> buffEffects = new LinkedList<>();
    private int maxDuration;
    
    public BuffModel(String name, String description, int maxDuration) {
        this.name = name;
        this.description = description;
        this.maxDuration = maxDuration;
    }
    
    public BuffModel addBuffEffect(BuffEffect buffEffect) {
        buffEffects.add(buffEffect);
        return this;
    }
    
    public List<BuffEffect> getBuffEffects() {
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
    
    public JSONObject toPayload() {
        JSONObject data = new JSONObject();
        data.put("name", getName());
        data.put("description", getDescription());
        data.put("maxDuration", getMaxDuration());
        JSONArray buffEffectsData = new JSONArray();
        buffEffects.forEach(item -> buffEffectsData.add(item.getType().name()));
        data.put("buffEffects", buffEffectsData);
        return data;
    }

}
