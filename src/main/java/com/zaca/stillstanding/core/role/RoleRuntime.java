package com.zaca.stillstanding.core.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zaca.stillstanding.core.skill.SkillSlot;
import com.zaca.stillstanding.dto.role.RoleConstInfoDTO;
import com.zaca.stillstanding.dto.role.RoleRuntimeInfoDTO;

/**
 * @author hundun
 * Created on 2019/10/26
 */
public class RoleRuntime {
    
    private final RolePrototype prototype;
    
    private Map<String, Integer> skillRemainTimes = new HashMap<>();

    public RoleRuntime(RolePrototype prototype) {
        this.prototype = prototype;
        resetForMatch();
    }
    
    public void resetForMatch() {
        skillRemainTimes.clear();
        prototype.getSkillSlots().forEach(skillSlot -> skillRemainTimes.put(skillSlot.getSkill().getName(), skillSlot.getFullCount()));
    }
    
    public boolean canUseOnce(String skillName) {
        return skillRemainTimes.containsKey(skillName) && skillRemainTimes.get(skillName) > 0;
    }
    
    public boolean useOnce(String skillName) {
        if (canUseOnce(skillName)) {
            skillRemainTimes.compute(skillName, (k,v) -> v-1);
            return true;
        }
        return false;
    }
    
    public Map<String, Integer> getSkillRemainTimes() {
        return skillRemainTimes;
    }

    public RoleRuntimeInfoDTO toRoleRuntimeInfoDTO() {
        RoleRuntimeInfoDTO dto = new RoleRuntimeInfoDTO();
        dto.setName(prototype.getName());
        dto.setSkillRemainTimes(this.skillRemainTimes);
        return dto;
    }
    
    
    
    public RolePrototype getPrototype() {
        return prototype;
    }
    
}
