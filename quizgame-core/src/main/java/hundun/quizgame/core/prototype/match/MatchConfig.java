package hundun.quizgame.core.prototype.match;

import java.util.List;

import lombok.Data;
/**
 * @author hundun
 * Created on 2021/05/08
 */
@Data
public class MatchConfig {
    MatchStrategyType matchStrategyType;
    List<String> teamNames;
    String questionPackageName;
}
