package hundun.quizgame.core.model.skill;
/**
 * @author hundun
 * Created on 2019/10/08
 */
public class SkillSlot {
    
    private final SkillConstData skill;
    private final int fullCount;
    
    public SkillSlot(SkillConstData skill, int fullCount) {
        this.skill = skill;
        this.fullCount = fullCount;
    }

    public SkillConstData getSkill() {
        return skill;
    }
    
    public int getFullCount() {
        return fullCount;
    }

}
