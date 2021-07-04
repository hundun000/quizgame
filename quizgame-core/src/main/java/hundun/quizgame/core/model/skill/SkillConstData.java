package hundun.quizgame.core.model.skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hundun.quizgame.core.exception.ModFormatException;
import hundun.quizgame.core.model.skill.effect.ISkillEffect;

/**
 * 原型类
 * @author hundun
 * Created on 2019/10/08
 */
public class SkillConstData {
    
    private final String name;
    private final String description;
    private final List<String> eventArgs;
    private final List<ISkillEffect> backendEffects;
    
    SkillConstData(String name, String description, List<String> frontendData, ISkillEffect... backendEffects) throws ModFormatException {
        this.name = name;
        this.description = description;
        
        this.eventArgs = frontendData;
        
        if (backendEffects != null) {
            this.backendEffects = Arrays.asList(backendEffects);
        } else {
            this.backendEffects = new ArrayList<>(0);
        }
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<String> getEventArgs() {
        return eventArgs;
    }
    
    public List<ISkillEffect> getBackendEffects() {
        return backendEffects;
    }
    
}
