package com.zaca.stillstanding.domain.skill;
/**
 * @author hundun
 * Created on 2019/10/08
 */
public class BaseSkill {
    
    private final String name;
    private final String description;
    
    public BaseSkill(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
}
