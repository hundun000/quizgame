package hundun.quizgame.core.view.role;

import java.util.List;
import hundun.quizgame.core.model.domain.SkillSlotRuntimeModel;
import hundun.quizgame.core.view.skill.SkillSlotRuntimeView;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/06/28
 */
@Data
public class RoleRuntimeView {
    String name;
    List<SkillSlotRuntimeView> skillSlotRuntimeViews;
}
