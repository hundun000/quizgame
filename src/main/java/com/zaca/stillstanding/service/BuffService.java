package com.zaca.stillstanding.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.zaca.stillstanding.core.buff.BuffModel;
import com.zaca.stillstanding.core.buff.RunTimeBuff;
import com.zaca.stillstanding.core.buff.ScoreComboBuffEffect;
import com.zaca.stillstanding.exception.NotFoundException;

/**
 * @author hundun
 * Created on 2020/05/25
 */
@Service
public class BuffService {
    
    private  Map<String, BuffModel> buffModels = new HashMap<>();
    
    public BuffService() {
        BuffModel buffModel;
        buffModel = new BuffModel("连击中", "答题正确时，额外获得与“连击中”层数相同的分数，且“连击中”层数加1（最大为3层）；否则，失去所有“连击中”层数。", 3);
        buffModel.addBuffEffect(new ScoreComboBuffEffect());
        buffModels.put(buffModel.getName(), buffModel);
        
    }
    
    
    public RunTimeBuff generateRunTimeBuff(String buffModelName, int duration) throws NotFoundException {
        if (!buffModels.containsKey(buffModelName)) {
            throw new NotFoundException("Buff", buffModelName);
        }
        return new RunTimeBuff(buffModels.get(buffModelName), duration);
    }
    
    public Collection<BuffModel> listBuffModels() {
        return buffModels.values();
    }

}
