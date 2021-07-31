package hundun.quizgame.core.context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import hundun.quizgame.core.model.domain.match.strategy.MatchStrategyFactory;
import hundun.quizgame.core.service.BuffService;
import hundun.quizgame.core.service.BuiltinDataConfiguration;
import hundun.quizgame.core.service.GameService;
import hundun.quizgame.core.service.QuestionLoaderService;
import hundun.quizgame.core.service.QuestionService;
import hundun.quizgame.core.service.RoleSkillService;
import hundun.quizgame.core.service.SessionService;
import hundun.quizgame.core.service.TeamService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/08/03
 */
@Slf4j
public class QuizCoreContext {
    
    public TeamService teamService;
    public QuestionService questionService;
    public RoleSkillService roleSkillService;
    public BuffService buffService;
    public MatchStrategyFactory matchStrategyFactory;
    public SessionService sessionService;
    public QuestionLoaderService questionLoaderService;
    public GameService gameService;
    private BuiltinDataConfiguration builtinDataConfiguration;
//    public void postConstruct(QuizCoreContext quizCoreContext) {
//        this.teamService = quizCoreContext.teamService;
//        this.questionService = quizCoreContext.questionService;
//        this.roleSkillService = quizCoreContext.roleSkillService;
//        this.buffService = quizCoreContext.buffService;
//        this.matchStrategyFactory = quizCoreContext.matchStrategyFactory;
//        this.sessionService = quizCoreContext.sessionService;
//        this.questionLoaderService = quizCoreContext.questionLoaderService;
//    }
    
    public QuizCoreContext() {
        this.teamService = new TeamService();
        this.questionService = new QuestionService();
        this.roleSkillService = new RoleSkillService();
        this.buffService = new BuffService();
        this.matchStrategyFactory = new MatchStrategyFactory();
        this.sessionService = new SessionService();
        this.questionLoaderService = new QuestionLoaderService();
        this.gameService = new GameService();
        
        this.builtinDataConfiguration = new BuiltinDataConfiguration();
        
        init();
    }
    

    
    private void init() {
        
        StringBuilder namesBuilder = new StringBuilder();
        
        List<IQuizCoreComponent> components = new ArrayList<>();
        
        Class<?> clazz = this.getClass();   
        Field[] fields = clazz.getDeclaredFields();   
        for (Field field : fields) {
            if (IQuizCoreComponent.class.isAssignableFrom(field.getType())) {
                try {
                    IQuizCoreComponent bean = (IQuizCoreComponent) field.get(this);
                    components.add(bean);
                    namesBuilder.append(bean.getClass().getSimpleName()).append(", ");
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }    
        }
        
        log.info("components: {}", namesBuilder.toString());
        
        for (IQuizCoreComponent component : components) {
            try {
                component.wire(this);
            } catch (Exception e) {
                log.error("component error: ", e);
            }
        }
        
        for (IQuizCoreComponent component : components) {
            try {
                component.postWired();
            } catch (Exception e) {
                log.error("component error: ", e);
            }
        }
    }
    
    
    
}
