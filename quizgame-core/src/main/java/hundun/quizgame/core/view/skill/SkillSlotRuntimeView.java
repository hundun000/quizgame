package hundun.quizgame.core.view.skill;

import java.util.List;

import hundun.quizgame.core.model.domain.SkillSlotRuntimeModel;
import hundun.quizgame.core.prototype.skill.SkillSlotPrototype;
import hundun.quizgame.core.view.role.RoleRuntimeView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2021/07/17
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SkillSlotRuntimeView {
    private String name;
    private int remainUseTime;
}
