package com.zaca.stillstanding.domain.buff;

import java.util.HashMap;
import java.util.Map;

import com.zaca.stillstanding.domain.skill.BaseSkill;
import com.zaca.stillstanding.exception.NotFoundException;

/**
 * @author hundun
 * Created on 2020/05/20
 */
public class BuffFactory {

    private static Map<String, BuffModel> buffModels = new HashMap<>();
    static {
        BuffModel buffModel;
        
        buffModel = new BuffModel("连击中", "答题正确时，额外获得与“连击中”层数相同的分数，且“连击中”层数加1（最大为3层）；否则，失去所有“连击中”层数。", 3);
        buffModel.addBuffEffect(new ScoreComboBuffEffect());
        buffModels.put(buffModel.getName(), buffModel);
        
    }
    
    
    public static RunTimeBuff getBuff(String buffName, int duration) throws NotFoundException {
        if (!buffModels.containsKey(buffName)) {
            throw new NotFoundException("Buff", buffName);
        }
        return new RunTimeBuff(buffModels.get(buffName), duration);
    }
}
