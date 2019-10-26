package com.zaca.stillstanding.domain.skill;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hundun
 * Created on 2019/10/26
 */
public class RoleRunTimeData {
    
    Map<String, Integer> skillCounters = new HashMap<>();

    public RoleRunTimeData(BaseRole role) {
        role.getSkillSlots().forEach(skillSlot -> skillCounters.put(skillSlot.getSkill().getName(), skillSlot.getFullCount()));
    }
    
    public void resetCount() {
        skillCounters.replaceAll((k, v) -> 0);
    }
    
    public boolean useOnce(String skillName) {
        if (skillCounters.containsKey(skillName) && skillCounters.get(skillName) > 0) {
            skillCounters.compute(skillName, (k,v) -> v-1);
            return true;
        }
        return false;
    }
    
    public Map<String, Integer> getSkillCounters() {
        return skillCounters;
    }

}
