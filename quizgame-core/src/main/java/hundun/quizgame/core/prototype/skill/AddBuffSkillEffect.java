package hundun.quizgame.core.prototype.skill;


import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hundun
 * Created on 2020/05/20
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddBuffSkillEffect {
    
    private String buffName;
    private int duration;
    

}
