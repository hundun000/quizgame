package hundun.quizgame.core.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import hundun.quizgame.core.exception.NotFoundException;
import hundun.quizgame.core.model.domain.buff.BuffModel;
import hundun.quizgame.core.prototype.buff.BuffPrototype;


/**
 * @author hundun
 * Created on 2020/05/25
 */
@Service
public class BuffService {
    
    private  Map<String, BuffPrototype> buffPrototypes = new HashMap<>();
    
    public BuffService() {
        
        
    }
    
    public void registerBuffPrototype(BuffPrototype prototype) {
        buffPrototypes.put(prototype.getName(), prototype);
    }
    
    
    public BuffModel generateRunTimeBuff(String buffPrototypeName, int duration) throws NotFoundException {
        if (!buffPrototypes.containsKey(buffPrototypeName)) {
            throw new NotFoundException("Buff", buffPrototypeName);
        }
        return new BuffModel(buffPrototypes.get(buffPrototypeName), duration);
    }
    
    public Collection<BuffPrototype> listBuffModels() {
        return buffPrototypes.values();
    }

}
