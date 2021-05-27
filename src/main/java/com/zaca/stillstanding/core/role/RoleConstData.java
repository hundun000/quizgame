package com.zaca.stillstanding.core.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zaca.stillstanding.core.skill.SkillConstData;
import com.zaca.stillstanding.core.skill.SkillSlot;
import com.zaca.stillstanding.dto.role.RoleConstInfoDTO;
import com.zaca.stillstanding.exception.StillStandingException;

/**
 * @author hundun
 * Created on 2019/10/08
 */
public class RoleConstData {
    
    private final String name;
    private final String description;
    private List<SkillSlot> skillSlots;
    
    
    public RoleConstData(String name, String description, SkillSlot... skillSlots) {
        this.name = name;
        this.description = description;
        this.skillSlots = Arrays.asList(skillSlots);
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<SkillSlot> getSkillSlots() {
        return skillSlots;
    }
    
    public SkillConstData getSkill(String skillName) throws StillStandingException {
        
        for (SkillSlot skillSlot : this.getSkillSlots()) {
            if (skillSlot.getSkill().getName().equals(skillName)) {
                return skillSlot.getSkill();
            }
        }
        throw new StillStandingException(this.getName() + "里没有技能：" + skillName, -1);
    }
    
    public RoleConstInfoDTO toRoleConstInfoDTO() {
        RoleConstInfoDTO dto = new RoleConstInfoDTO();
        dto.setName(this.name);
        dto.setDescription(this.description);
        List<String> skillNames = new ArrayList<>(this.skillSlots.size());
        List<String> skillDescriptions = new ArrayList<>(this.skillSlots.size());
        List<Integer> skillFullCounts = new ArrayList<>(this.skillSlots.size());
        for (SkillSlot skillSlot : skillSlots) {
            skillNames.add(skillSlot.getSkill().getName());
            skillDescriptions.add(skillSlot.getSkill().getDescription());
            skillFullCounts.add(skillSlot.getFullCount());
        }
        dto.setSkillNames(skillNames);
        dto.setSkillDescriptions(skillDescriptions);
        dto.setSkillFullCounts(skillFullCounts);
        return dto;
    }

}
