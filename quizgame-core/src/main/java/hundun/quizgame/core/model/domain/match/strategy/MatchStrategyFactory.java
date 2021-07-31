package hundun.quizgame.core.model.domain.match.strategy;

import hundun.quizgame.core.context.IQuizCoreComponent;
import hundun.quizgame.core.context.QuizCoreContext;
import hundun.quizgame.core.exception.NotFoundException;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.prototype.match.MatchStrategyType;
import hundun.quizgame.core.service.BuffService;
import hundun.quizgame.core.service.QuestionService;
import hundun.quizgame.core.service.RoleSkillService;
import hundun.quizgame.core.service.TeamService;

/**
 * @author hundun
 * Created on 2021/06/28
 */
public class MatchStrategyFactory implements IQuizCoreComponent {
    
    private QuestionService questionService;
    private TeamService teamService;
    private RoleSkillService roleSkillService;
    private BuffService buffService;
    
    @Override
    public void wire(QuizCoreContext quizCoreContext) {
        this.teamService = quizCoreContext.teamService;
        this.questionService = quizCoreContext.questionService;
        this.roleSkillService = quizCoreContext.roleSkillService;
        this.buffService = quizCoreContext.buffService;
    }
    
    public BaseMatchStrategy getMatchStrategy(MatchStrategyType type) throws QuizgameException {
        switch (type) {
            case ENDLESS:
                return new EndlessStrategy(questionService, teamService, roleSkillService, buffService);
            case PRE:
                return new PreStrategy(questionService, teamService, roleSkillService, buffService);
            case MAIN:
                return new MainStrategy(questionService, teamService, roleSkillService, buffService);
            default:
                throw new NotFoundException("MatchStrategyType", type.name());
        }
    }
}
