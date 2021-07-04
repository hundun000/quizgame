package hundun.quizgame.core.model.match.strategy;

import hundun.quizgame.core.dto.event.SwitchTeamEvent;
import hundun.quizgame.core.model.team.HealthType;
import hundun.quizgame.core.service.BuffService;
import hundun.quizgame.core.service.QuestionService;
import hundun.quizgame.core.service.RoleSkillService;
import hundun.quizgame.core.service.TeamService;

/**
 * @author hundun
 * Created on 2019/09/06
 */
public class PreStrategy extends BaseMatchStrategy {
    

    public PreStrategy(
            QuestionService questionService, 
            TeamService teamService, 
            RoleSkillService roleSkillService,
            BuffService buffService
            ) {
        super(questionService, teamService, roleSkillService, buffService,
                HealthType.SUM
                );
    }
    

    
    @Override
    public SwitchTeamEvent checkSwitchTeamEvent() {
        /*
         * 一定不换队
         */
        return null;
    }



    @Override
    public int calculateCurrentHealth() {
        /*
         * 累计答n题后死亡
         */
        int fullHealth = 5;
        int currentHealth = fullHealth - parent.getRecorder().countSum(parent.getCurrentTeam().getName(), fullHealth);
        
        return currentHealth;
    }

    

}
