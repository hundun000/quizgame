package hundun.quizgame.core.model.domain;

import java.util.ArrayList;
import java.util.List;

import hundun.quizgame.core.prototype.RolePrototype;
import hundun.quizgame.core.view.role.RoleRuntimeView;
import hundun.quizgame.core.view.skill.SkillSlotRuntimeView;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2019/10/26
 */
@Getter
public class RoleRuntimeModel {
    
    private final RolePrototype prototype;

    private List<SkillSlotRuntimeModel> skillSlotRuntimeModels;
    
    public RoleRuntimeModel(RolePrototype prototype) {
        this.prototype = prototype;
        skillSlotRuntimeModels.clear();
        prototype.getSkillSlotPrototypes().forEach(skillSlotPrototype -> skillSlotRuntimeModels.add(new SkillSlotRuntimeModel(skillSlotPrototype)));
    }
    
    

    public RoleRuntimeView toRoleRuntimeInfoDTO() {
        RoleRuntimeView dto = new RoleRuntimeView();
        dto.setName(prototype.getName());
        List<SkillSlotRuntimeView> skillSlotRuntimeViews = new ArrayList<>();
        skillSlotRuntimeModels.forEach(model -> skillSlotRuntimeViews.add(new SkillSlotRuntimeView(model.getPrototype().getName(), model.getRemainUseTime())));
        dto.setSkillSlotRuntimeViews(skillSlotRuntimeViews);
        return dto;
    }

    
}
