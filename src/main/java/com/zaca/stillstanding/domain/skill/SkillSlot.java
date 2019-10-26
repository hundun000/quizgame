package com.zaca.stillstanding.domain.skill;
/**
 * @author hundun
 * Created on 2019/10/08
 */
public class SkillSlot {
    
    private final BaseSkill skill;
    private final int fullCount;
    
    public SkillSlot(BaseSkill skill, int fullCount) {
        this.skill = skill;
        this.fullCount = fullCount;
    }

    public BaseSkill getSkill() {
        return skill;
    }
    
    public int getFullCount() {
        return fullCount;
    }

}
