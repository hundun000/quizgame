package hundun.quizgame.core.model.buff;

import java.util.LinkedList;
import java.util.List;

import hundun.quizgame.core.model.buff.effect.BuffEffect;

/**
 * 技能的持续性/延迟性效果。debuff也视为一种Buff。本类是Buff的享元。
 * @author hundun
 * Created on 2020/05/20
 */
public class BuffPrototype {
    private String name;
    private String description;
    List<BuffEffect> buffEffects = new LinkedList<>();
    private int maxDuration;
    
    public BuffPrototype(String name, String description, int maxDuration) {
        this.name = name;
        this.description = description;
        this.maxDuration = maxDuration;
    }
    
    public BuffPrototype addBuffEffect(BuffEffect buffEffect) {
        buffEffects.add(buffEffect);
        return this;
    }
    
    public List<BuffEffect> getBuffEffects() {
        return buffEffects;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getMaxDuration() {
        return maxDuration;
    }


}
