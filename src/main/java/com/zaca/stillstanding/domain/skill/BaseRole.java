package com.zaca.stillstanding.domain.skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zaca.stillstanding.exception.StillStandingException;

/**
 * @author hundun
 * Created on 2019/10/08
 */
public class BaseRole {
    
    private final String name;
    private final String description;
    private List<SkillSlot> skillSlots;
    
    
    public BaseRole(String name, String description, SkillSlot... skillSlots) {
        this.name = name;
        this.description = description;
        this.skillSlots = Arrays.asList(skillSlots);
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<SkillSlot> getSkillSlots() {
        return skillSlots;
    }
    
    public BaseSkill getSkill(String skillName) throws StillStandingException {
        for (SkillSlot skillSlot : skillSlots) {
            if (skillSlot.getSkill().getName().equals(skillName)) {
                return skillSlot.getSkill();
            }
        }
        throw new StillStandingException(getName() + "里没有技能：" + skillName, -1);
    }

}
