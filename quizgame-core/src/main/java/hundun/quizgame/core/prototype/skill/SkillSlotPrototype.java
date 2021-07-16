package hundun.quizgame.core.prototype.skill;
/**
 * @author hundun
 * Created on 2019/10/08
 */

import java.util.List;

import hundun.quizgame.core.prototype.RolePrototype;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SkillSlotPrototype {
    
    private String name;
    private String description;
    private List<String> eventArgs;
    private List<AddBuffSkillEffect> backendEffects;
    private int fullUseTime;


}
