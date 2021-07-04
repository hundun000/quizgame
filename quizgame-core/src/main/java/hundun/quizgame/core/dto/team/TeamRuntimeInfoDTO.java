package hundun.quizgame.core.dto.team;

import java.util.List;
import java.util.Map;

import hundun.quizgame.core.dto.buff.BuffRuntimeDTO;
import hundun.quizgame.core.dto.role.RoleRuntimeInfoDTO;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class TeamRuntimeInfoDTO {
    String name;
    int matchScore;
    RoleRuntimeInfoDTO roleRuntimeInfo;
    List<BuffRuntimeDTO> runtimeBuffs;
    boolean alive;
    int health;
}
