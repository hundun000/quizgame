package hundun.quizgame.core.model.domain.buff;


import hundun.quizgame.core.prototype.buff.BuffStrategyType;
import hundun.quizgame.core.prototype.buff.BuffPrototype;
import hundun.quizgame.core.view.buff.BuffRuntimeView;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2020/05/20
 */

public class BuffModel {
    @Getter
    private final BuffPrototype prototype;
    @Getter
    private int duration;
    
    @Getter
    private IBuffStrategy buffStrategy;
    
    public BuffModel(BuffPrototype prototype, int duration) {
        this.prototype = prototype;
        this.duration = duration;
        
        if (prototype.getBuffStrategyType() == BuffStrategyType.SCORE_MODIFY) {
            this.buffStrategy = new CombBuffStrategy();
        }
    }
    
    public void minusOneDurationAndCheckMaxDuration() {
        duration--;
        if (duration > prototype.getMaxDuration()) {
            duration = prototype.getMaxDuration();
        }
    }
    
    public void clearDuration() {
        duration = 0;
    }
    
    public void addDuration(int plus) {
        duration += plus;
    }
    


    public BuffRuntimeView toRunTimeBuffDTO() {
        BuffRuntimeView dto = new BuffRuntimeView();
        dto.setName(this.prototype.getName());
        dto.setDescription(this.prototype.getDescription());
        dto.setDuration(duration);
        return dto;
    }

}
