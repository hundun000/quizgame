package hundun.quizgame.core.prototype.event;

import java.util.List;

import hundun.quizgame.core.prototype.TeamPrototype;
import lombok.Data;
/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class StartMatchEvent extends MatchEvent {
    List<TeamPrototype> teamPrototypes;
}
