package hundun.quizgame.core.tool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import hundun.quizgame.core.dto.match.ClientActionType;
import hundun.quizgame.core.dto.match.MatchState;

/**
 * @author hundun
 * Created on 2021/06/29
 */
public class MatchStateUtils {
    
    static Map<MatchState, Set<ClientActionType>> stateTransitionRules;
    static {
        stateTransitionRules = new HashMap<>();
        addStateTransitionRule(MatchState.WAIT_START, ClientActionType.START_MATCH);
        addStateTransitionRule(MatchState.WAIT_GENERATE_QUESTION, ClientActionType.NEXT_QUESTION);
        addStateTransitionRule(MatchState.WAIT_ANSWER, ClientActionType.ANSWER);
        addStateTransitionRule(MatchState.WAIT_ANSWER, ClientActionType.USE_SKILL);
    }
    
    private static void addStateTransitionRule(MatchState state, ClientActionType actionType) {
        if (!stateTransitionRules.containsKey(state)) {
            stateTransitionRules.put(state, new HashSet<>(4));
        }
        stateTransitionRules.get(state).add(actionType);
    }
    
    
    public static Set<ClientActionType> getValidClientActions(MatchState state) {
        return stateTransitionRules.getOrDefault(state, new HashSet<>(0));
    }
    
    public static boolean check(MatchState state, ClientActionType actionType) {
        return stateTransitionRules.get(state).contains(actionType);
    }
}
