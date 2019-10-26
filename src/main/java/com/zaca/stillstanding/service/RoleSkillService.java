package com.zaca.stillstanding.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zaca.stillstanding.domain.skill.BaseRole;
import com.zaca.stillstanding.domain.skill.BaseSkill;
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
        registerSkill(new BaseSkill("5050", "揭示x个错误选项。", "{\"x\":2}"));
        registerSkill(new BaseSkill("跳过", "结束本题。本题不计入得分、答对数、答错数。"));
        registerSkill(new BaseSkill("求助", "答题时间增加x，并且本题期间可与毒奶团交流。", "{\"x\":30}"));
        registerSkill(new BaseSkill("加时", "答题时间增加x。", "{\"x\":15}"));
        
        BaseRole role;
        role = new BaseRole("ZACA娘", "主人公。");
        role.getSkillSlots().add(new SkillSlot(getSkill("5050"), 2));
        role.getSkillSlots().add(new SkillSlot(getSkill("求助"), 2));
        role.getSkillSlots().add(new SkillSlot(getSkill("跳过"), 2));
        role.getSkillSlots().add(new SkillSlot(getSkill("加时"), 2));
        
        roles.put(role.getName(), role);
    }
    private void registerSkill(BaseSkill skill) {
        skills.put(skill.getName(), skill);
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
    
    public boolean exsistRole(String name) {
        return roles.containsKey(name);
    }
    
    public Collection<BaseRole> listRoles() {
        return roles.values();
    }
}
