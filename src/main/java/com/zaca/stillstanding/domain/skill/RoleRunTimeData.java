package com.zaca.stillstanding.domain.skill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hundun
 * Created on 2019/10/26
 */
public class RoleRunTimeData {
    
    private Map<String, Integer> skillFull = new HashMap<>();
    private Map<String, Integer> skillRemain = new HashMap<>();

    public RoleRunTimeData(BaseRole role) {
        role.getSkillSlots().forEach(skillSlot -> skillFull.put(skillSlot.getSkill().getName(), skillSlot.getFullCount()));
        resetRemain();
    }
    
    public void resetRemain() {
        skillRemain.putAll(skillFull);
    }
    
    public boolean useOnce(String skillName) {
        if (skillRemain.containsKey(skillName) && skillRemain.get(skillName) > 0) {
            skillRemain.compute(skillName, (k,v) -> v-1);
            return true;
        }
        return false;
    }
    
    
}
