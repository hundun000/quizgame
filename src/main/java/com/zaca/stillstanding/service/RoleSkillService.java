package com.zaca.stillstanding.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zaca.stillstanding.core.role.RoleConstData;
import com.zaca.stillstanding.core.skill.BaseSkillFactory;
import com.zaca.stillstanding.core.skill.SkillSlot;

/**
 * @author hundun
 * Created on 2019/10/14
 */
@Service
public class RoleSkillService {
    
    Map<String, RoleConstData> roles = new HashMap<>();
    
    
    public RoleSkillService() {
        
        registerRole(new RoleConstData("ZACA娘", "主人公。",
                new SkillSlot(BaseSkillFactory.getSkill("5050"), 2),
                new SkillSlot(BaseSkillFactory.getSkill("求助"), 2),
                new SkillSlot(BaseSkillFactory.getSkill("跳过"), 2),
                new SkillSlot(BaseSkillFactory.getSkill("连击之力"), 2)
                )
        );

    }

    
    private void registerRole(RoleConstData role) {
        roles.put(role.getName(), role);
    }

    

    public RoleConstData getRole(String name) {
        return roles.get(name);
    }
    
    public boolean existRole(String name) {
        return roles.containsKey(name);
    }
    
    public Collection<RoleConstData> listRoles() {
        return roles.values();
    }
}
