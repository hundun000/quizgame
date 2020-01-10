package com.zaca.stillstanding.domain.skill;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hundun
 * Created on 2019/10/08
 */
public class BaseRole {
    
    private final String name;
    private final String description;
    private List<SkillSlot> skillSlots = new ArrayList<>();
    
    
    public BaseRole(String name, String description) {
        this.name = name;
        this.description = description;
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
    
    public BaseSkill getSkill(String skillName) {
        for (SkillSlot skillSlot : skillSlots) {
            if (skillSlot.getSkill().getName().equals(skillName)) {
                return skillSlot.getSkill();
            }
        }
        return null;
    }

}
