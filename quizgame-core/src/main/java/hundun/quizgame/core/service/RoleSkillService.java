package hundun.quizgame.core.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import hundun.quizgame.core.context.IQuizCoreComponent;
import hundun.quizgame.core.prototype.RolePrototype;
import hundun.quizgame.core.prototype.skill.SkillSlotPrototype;

/**
 * @author hundun
 * Created on 2019/10/14
 */
public class RoleSkillService {
    
    Map<String, RolePrototype> roles = new HashMap<>();
    Map<String, SkillSlotPrototype> skills = new HashMap<>();
    
    public RoleSkillService() {
        
        

    }

    
    public void registerRole(RolePrototype role) {
        roles.put(role.getName(), role);
    }

    public void registerSkill(SkillSlotPrototype skill) {
        skills.put(skill.getName(), skill);
    }
    
    public SkillSlotPrototype getSkillSlotPrototype(String name) {
        return skills.get(name);
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
