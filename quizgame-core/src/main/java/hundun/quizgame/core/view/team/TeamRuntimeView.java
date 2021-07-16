package hundun.quizgame.core.view.team;

import java.util.List;
import hundun.quizgame.core.view.buff.BuffRuntimeView;
import hundun.quizgame.core.view.role.RoleRuntimeView;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class TeamRuntimeView {
    String name;
    int matchScore;
    RoleRuntimeView roleRuntimeInfo;
    List<BuffRuntimeView> runtimeBuffs;
    boolean alive;
    int health;
}
