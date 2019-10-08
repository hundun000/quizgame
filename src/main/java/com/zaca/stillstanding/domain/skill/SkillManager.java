package com.zaca.stillstanding.domain.skill;
/**
 * @author hundun
 * Created on 2019/10/08
 */

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SkillManager {
    
    Map<String, BaseSkill> skills = new HashMap<>();
    
    public SkillManager() {
        registerSkill(new BaseSkill("5050", "揭示两个错误选项。"));
        registerSkill(new BaseSkill("跳过", "结束本题。不计入得分，答对，答错。"));
        registerSkill(new BaseSkill("求助", "答题时间增加，并且本题期间可与毒奶团交流。"));
    }
    private void registerSkill(BaseSkill skill) {
        skills.put(skill.getName(), skill);
    }
    
    
    public BaseSkill get(String skillName) {
        return skills.get(skillName);
    }
    public boolean skillExsist(String skillName) {
        return skills.containsKey(skillName);
    }
}
