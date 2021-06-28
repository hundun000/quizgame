package com.zaca.stillstanding.core.team;

import java.util.List;
import java.util.Set;

import com.zaca.stillstanding.core.role.RolePrototype;
import com.zaca.stillstanding.core.role.RoleRuntime;
import com.zaca.stillstanding.dto.team.TeamConstInfoDTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2021/06/28
 */
@Getter
public class TeamPrototype {
    private final String name;
    @Setter
    private List<String> pickTags;
    @Setter
    private List<String> banTags;
    @Setter
    private RolePrototype rolePrototype;
    
    public TeamPrototype(String name) {
        this.name = name;
    }
    
    public boolean isPickAndNotBan(Set<String> questionTags) {
        for (String questionTag:questionTags) {
            if (pickTags.contains(questionTag) && !banTags.contains(questionTag)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isNotBan(Set<String> questionTags) {
        for (String questionTag:questionTags) {
            if (banTags.contains(questionTag)) {
                return false;
            }
        }
        return true;
    }
    
    public TeamConstInfoDTO toTeamConstInfoDTO() {
        TeamConstInfoDTO teamConstInfoDTO = new TeamConstInfoDTO();
        teamConstInfoDTO.setName(getName());
        teamConstInfoDTO.setPickTags(this.getPickTags());
        teamConstInfoDTO.setBanTags(this.getBanTags());
        teamConstInfoDTO.setRoleName(rolePrototype != null ? rolePrototype.getName() : null);
        return teamConstInfoDTO;
    }
    
}
