package com.zaca.stillstanding.domain.skill;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hundun
 * Created on 2019/10/08
 */
@Component
public class RoleManager {
    
    @Autowired
    SkillManager skillManager;
    
    Map<String, BaseRole> roles = new HashMap<>();
    
    public RoleManager() {
        
    }
    
    @PostConstruct
    private void post(){
        BaseRole role;
        role = new BaseRole("ZACA娘", "主人公。");
        role.getSkillSlots().add(new SkillSlot(skillManager.get("5050"), 2));
        role.getSkillSlots().add(new SkillSlot(skillManager.get("求助"), 2));
        role.getSkillSlots().add(new SkillSlot(skillManager.get("跳过"), 2));
        
        
        roles.put(role.getName(), role);
    }
    
    
    
    public BaseRole get(String name) {
        return roles.get(name);
    }
    
    public boolean exsist(String name) {
        return roles.containsKey(name);
    }

}
