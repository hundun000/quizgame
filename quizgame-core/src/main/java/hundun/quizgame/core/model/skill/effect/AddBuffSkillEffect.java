package hundun.quizgame.core.model.skill.effect;
/**
 * @author hundun
 * Created on 2020/05/20
 */
public class AddBuffSkillEffect implements ISkillEffect {
    
    private String buffName;
    private int duration;
    
    public AddBuffSkillEffect(String buffName, int duration) {
        super();
        this.buffName = buffName;
        this.duration = duration;
    }
    
    
    public String getBuffName() {
        return buffName;
    }
    
    public int getDuration() {
        return duration;
    }
   
   

}
