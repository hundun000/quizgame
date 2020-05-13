package com.zaca.stillstanding.domain.skill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hundun
 * Created on 2019/10/26
 */
public class RoleRuntimeData {
    
    private Map<String, Integer> skillRemainTimes = new HashMap<>();

    public RoleRuntimeData(BaseRole role) {
        resetRemain(role);
    }
    
    public void resetRemain(BaseRole role) {
        skillRemainTimes.clear();
        role.getSkillSlots().forEach(skillSlot -> skillRemainTimes.put(skillSlot.getSkill().getName(), skillSlot.getFullCount()));
    }
    
    public boolean useOnce(String skillName) {
        if (skillRemainTimes.containsKey(skillName) && skillRemainTimes.get(skillName) > 0) {
            skillRemainTimes.compute(skillName, (k,v) -> v-1);
            return true;
        }
        return false;
    }
    
    public Map<String, Integer> getSkillRemainTimes() {
        return skillRemainTimes;
    }
    
    
}
