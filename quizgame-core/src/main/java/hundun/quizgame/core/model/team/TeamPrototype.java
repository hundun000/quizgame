package hundun.quizgame.core.model.team;

import java.util.List;
import java.util.Set;

import hundun.quizgame.core.dto.team.TeamConstInfoDTO;
import hundun.quizgame.core.model.role.RolePrototype;
import hundun.quizgame.core.model.role.RoleRuntime;
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
