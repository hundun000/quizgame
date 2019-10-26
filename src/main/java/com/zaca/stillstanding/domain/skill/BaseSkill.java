package com.zaca.stillstanding.domain.skill;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 原型类
 * @author hundun
 * Created on 2019/10/08
 */
public class BaseSkill {
    
    private final String name;
    private final String description;
    private final JSONObject staticData;
    
    public BaseSkill(String name, String description) {
        this(name, description, null);
    }
    public BaseSkill(String name, String description, String staticDataString) {
        this.name = name;
        this.description = description;
        
        JSONObject staticData = null;
        if (staticDataString != null) {
            try {
                staticData = JSONObject.parseObject(staticDataString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.staticData = staticData;
        
        
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public JSONObject getStaticData() {
        return staticData;
    }
    
}
