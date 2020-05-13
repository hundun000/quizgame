package com.zaca.stillstanding.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zaca.stillstanding.domain.skill.BaseRole;
import com.zaca.stillstanding.domain.skill.BaseSkill;
import com.zaca.stillstanding.domain.skill.BaseSkillFactory;
import com.zaca.stillstanding.domain.skill.SkillSlot;

/**
 * @author hundun
 * Created on 2019/10/14
 */
@Service
public class RoleSkillService {
    
    Map<String, BaseSkill> skills = new HashMap<>();
    Map<String, BaseRole> roles = new HashMap<>();
    
    
    public RoleSkillService() {
        registerSkill(BaseSkillFactory.getSkill("5050"));
        registerSkill(BaseSkillFactory.getSkill("跳过"));
        registerSkill(BaseSkillFactory.getSkill("求助"));
        registerSkill(BaseSkillFactory.getSkill("加时"));
        
        registerRole(new BaseRole("ZACA娘", "主人公。",
                new SkillSlot(getSkill("5050"), 2),
                new SkillSlot(getSkill("求助"), 2),
                new SkillSlot(getSkill("跳过"), 2),
                new SkillSlot(getSkill("加时"), 2)
                )
        );

    }
    private void registerSkill(BaseSkill skill) {
        skills.put(skill.getName(), skill);
    }
    
    private void registerRole(BaseRole role) {
        roles.put(role.getName(), role);
    }
    
    public BaseSkill getSkill(String skillName) {
        return skills.get(skillName);
    }
    public boolean exsistSkill(String skillName) {
        return skills.containsKey(skillName);
    }

    public BaseRole getRole(String name) {
        return roles.get(name);
    }
    
    public boolean existRole(String name) {
        return roles.containsKey(name);
    }
    
    public Collection<BaseRole> listRoles() {
        return roles.values();
    }
}
