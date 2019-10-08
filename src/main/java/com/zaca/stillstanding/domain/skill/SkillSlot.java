package com.zaca.stillstanding.domain.skill;
/**
 * @author hundun
 * Created on 2019/10/08
 */
public class SkillSlot {
    
    private final BaseSkill skill;
    private int count;
    
    public SkillSlot(BaseSkill skill, int count) {
        this.skill = skill;
        this.count = count;
    }
    
}
