package com.zaca.stillstanding.domain.skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.skill.effect.ISkillEffect;
import com.zaca.stillstanding.exception.ModFormatException;

/**
 * 原型类
 * @author hundun
 * Created on 2019/10/08
 */
public class BaseSkill {
    
    private final String name;
    private final String description;
    private final List<String> frontendData;
    private final List<ISkillEffect> backendEffects;
    
    BaseSkill(String name, String description, List<String> frontendData, ISkillEffect... backendEffects) throws ModFormatException {
        this.name = name;
        this.description = description;
        
        this.frontendData = frontendData;
        
        if (backendEffects != null) {
            this.backendEffects = Arrays.asList(backendEffects);
        } else {
            this.backendEffects = new ArrayList<>(0);
        }
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<String> getFrontendData() {
        return frontendData;
    }
    
    public List<ISkillEffect> getBackendEffects() {
        return backendEffects;
    }
    
}
