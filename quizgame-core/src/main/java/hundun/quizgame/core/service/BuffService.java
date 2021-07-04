package hundun.quizgame.core.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import hundun.quizgame.core.exception.NotFoundException;
import hundun.quizgame.core.model.buff.BuffPrototype;
import hundun.quizgame.core.model.buff.BuffRuntime;
import hundun.quizgame.core.model.buff.effect.ScoreComboBuffEffect;

/**
 * @author hundun
 * Created on 2020/05/25
 */
@Service
public class BuffService {
    
    private  Map<String, BuffPrototype> buffPrototypes = new HashMap<>();
    
    public BuffService() {
        BuffPrototype buffPrototype;
        buffPrototype = new BuffPrototype("连击中", "答题正确时，额外获得与“连击中”层数相同的分数，且“连击中”层数加1（最大为3层）；否则，失去所有“连击中”层数。", 3);
        buffPrototype.addBuffEffect(new ScoreComboBuffEffect());
        buffPrototypes.put(buffPrototype.getName(), buffPrototype);
        
    }
    
    
    public BuffRuntime generateRunTimeBuff(String buffModelName, int duration) throws NotFoundException {
        if (!buffPrototypes.containsKey(buffModelName)) {
            throw new NotFoundException("Buff", buffModelName);
        }
        return new BuffRuntime(buffPrototypes.get(buffModelName), duration);
    }
    
    public Collection<BuffPrototype> listBuffModels() {
        return buffPrototypes.values();
    }

}
