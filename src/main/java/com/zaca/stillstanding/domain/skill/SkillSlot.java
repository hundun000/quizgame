package com.zaca.stillstanding.domain.skill;
/**
 * @author hundun
 * Created on 2019/10/08
 */
public class SkillSlot implements Cloneable{
    
    private final BaseSkill skill;
    private final int fullCount;
    private int count;
    
    public SkillSlot(BaseSkill skill, int fullCount) {
        this.skill = skill;
        this.fullCount = fullCount;
        resetCount();
    }

    public BaseSkill useOnce() {
        if (count > 0) {
            count--;
            return skill;
        }
        return null;
    }

    public BaseSkill getSkill() {
        return skill;
    }
    
    public void resetCount() {
        count = fullCount;
    }
    
    @Override
        protected SkillSlot clone() {
            return new SkillSlot(skill, fullCount);
        }
    
}
