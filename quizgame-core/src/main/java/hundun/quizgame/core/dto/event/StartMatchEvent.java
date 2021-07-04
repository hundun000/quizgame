package hundun.quizgame.core.dto.event;

import java.util.List;

import hundun.quizgame.core.dto.role.RoleConstInfoDTO;
import hundun.quizgame.core.dto.team.TeamConstInfoDTO;
import lombok.Data;
/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class StartMatchEvent extends MatchEvent {
    List<TeamConstInfoDTO> teamConstInfos;
    List<RoleConstInfoDTO> roleConstInfos;
}
