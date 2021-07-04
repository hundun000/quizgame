package hundun.quizgame.core.dto.role;

import java.util.Map;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/06/28
 */
@Data
public class RoleRuntimeInfoDTO {
    String name;
    Map<String, Integer> skillRemainTimes;
}
