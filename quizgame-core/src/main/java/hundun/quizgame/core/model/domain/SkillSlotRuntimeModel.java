package hundun.quizgame.core.model.domain;

import hundun.quizgame.core.prototype.skill.SkillSlotPrototype;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/07/17
 */
@Data
public class SkillSlotRuntimeModel {
    
    private SkillSlotPrototype prototype;
    private int remainUseTime;  
    
    public SkillSlotRuntimeModel(SkillSlotPrototype prototype) {
        this.prototype = prototype;
        this.remainUseTime = prototype.getFullUseTime();
    }

    public boolean canUseOnce(String skillName) {
        return remainUseTime > 0;
    }
    
    public boolean useOnce(String skillName) {
        if (canUseOnce(skillName)) {
            remainUseTime--;
            return true;
        }
        return false;
    }
}
