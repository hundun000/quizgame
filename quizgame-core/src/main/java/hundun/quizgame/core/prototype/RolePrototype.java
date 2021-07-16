package hundun.quizgame.core.prototype;

import java.util.List;

import hundun.quizgame.core.prototype.skill.SkillSlotPrototype;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * @author hundun
 * Created on 2019/10/08
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RolePrototype {
    
    private String name;
    private String description;
    private List<SkillSlotPrototype> skillSlotPrototypes;


}
