package hundun.quizgame.core.model.domain.match.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
public class MatchStrategyFactory {
    
    @Autowired
    private QuestionService questionService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private RoleSkillService roleSkillService;
    @Autowired
    private BuffService buffService;
    
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
