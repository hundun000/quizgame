package com.zaca.stillstanding.core.role;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hundun
 * Created on 2019/10/26
 */
public class RoleRuntimeData {
    
    private Map<String, Integer> skillRemainTimes = new HashMap<>();

    public RoleRuntimeData(RoleConstData role) {
        resetRemain(role);
    }
    
    public void resetRemain(RoleConstData role) {
        skillRemainTimes.clear();
        role.getSkillSlots().forEach(skillSlot -> skillRemainTimes.put(skillSlot.getSkill().getName(), skillSlot.getFullCount()));
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
    
    
}
