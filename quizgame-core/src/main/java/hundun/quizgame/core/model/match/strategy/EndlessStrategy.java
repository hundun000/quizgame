package hundun.quizgame.core.model.match.strategy;

import hundun.quizgame.core.dto.event.SwitchTeamEvent;
import hundun.quizgame.core.model.team.HealthType;
import hundun.quizgame.core.service.BuffService;
import hundun.quizgame.core.service.QuestionService;
import hundun.quizgame.core.service.RoleSkillService;
import hundun.quizgame.core.service.TeamService;

/**
 * @author hundun
 * Created on 2021/04/25
 */
public class EndlessStrategy extends BaseMatchStrategy {

    public EndlessStrategy(QuestionService questionService, TeamService teamService, RoleSkillService roleSkillService,
            BuffService buffService) {
        super(
                questionService, 
                teamService, 
                roleSkillService, 
                buffService, 
                HealthType.ENDLESS
                );
    }

    @Override
    public int calculateCurrentHealth() {
        /*
         * 一定不死亡
         */
        return 1;
    }


    @Override
    public SwitchTeamEvent checkSwitchTeamEvent() {
        /*
         * 一定不换队
         */
        return null;
    }

}
