package hundun.quizgame.core.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import hundun.quizgame.core.model.role.RolePrototype;
import hundun.quizgame.core.model.skill.BaseSkillFactory;
import hundun.quizgame.core.model.skill.SkillSlot;

/**
 * @author hundun
 * Created on 2019/10/14
 */
@Service
public class RoleSkillService {
    
    Map<String, RolePrototype> roles = new HashMap<>();
    
    
    public RoleSkillService() {
        
        registerRole(new RolePrototype("ZACA娘", "主人公。",
                new SkillSlot(BaseSkillFactory.getSkill("5050"), 2),
                new SkillSlot(BaseSkillFactory.getSkill("求助"), 2),
                new SkillSlot(BaseSkillFactory.getSkill("跳过"), 2),
                new SkillSlot(BaseSkillFactory.getSkill("连击之力"), 2)
                )
        );

    }

    
    private void registerRole(RolePrototype role) {
        roles.put(role.getName(), role);
    }

    

    public RolePrototype getRole(String name) {
        return roles.get(name);
    }
    
    public boolean existRole(String name) {
        return roles.containsKey(name);
    }
    
    public Collection<RolePrototype> listRoles() {
        return roles.values();
    }
}
