package com.zaca.stillstanding.domain.match;

import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.question.AnswerType;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.TeamDeadException;

/**
 * @author hundun
 * Created on 2019/09/10
 */
public class MainMatch extends BaseMatch {

    /**
     * 固定加1
     */
    @Override
    protected void addScore(AnswerType answerType) {
        if (answerType == AnswerType.CORRECT) {
            currentTeam.addScore(1);
        }
    }

    /**
     * 连续答错1题则死亡
     */
    @Override
    protected MatchEvent checkTeamDieEvent() {
        if (recorder.isConsecutiveWrongAtLeastByTeam(currentTeam.getName(), 1)) {
            currentTeam.setAlive(false);
            return MatchEvent.getTypeTeamDie(currentTeam);
        }
        return null;
    }

    /**
     * 每一题换队（被调用一定换）
     */
    @Override
    protected MatchEvent checkSwitchTeamEvent() {
        Team lastTeam = currentTeam;
        switchToNextTeam();
        return MatchEvent.getTypeSwitchTeam(lastTeam, currentTeam);
    }

}
