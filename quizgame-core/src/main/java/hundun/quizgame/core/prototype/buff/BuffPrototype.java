package hundun.quizgame.core.prototype.buff;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 技能的持续性/延迟性效果。debuff也视为一种Buff。
 * @author hundun
 * Created on 2020/05/20
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuffPrototype {
    private String name;
    private String description;
    private int maxDuration;
    BuffStrategyType buffStrategyType;
}
