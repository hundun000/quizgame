package hundun.quizgame.core.context;
/**
 * @author hundun
 * Created on 2021/08/03
 */
public interface IQuizCoreComponent {
    void wire(QuizCoreContext quizCoreContext);
    default void postWired() {};
}
