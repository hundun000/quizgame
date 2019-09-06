package com.zaca.stillstanding.domain.match;

import com.zaca.stillstanding.domain.team.Team;

/**
 * @author hundun
 * Created on 2019/09/06
 */
public class PreMatch extends CoupleMatch {
    
    protected static final int LOSE_SUM = 10;

    /**
     * 累计答n题后死亡
     */
    @Override
    protected MatchEvent checkTeamDieEvent() {
        if (recorder.isSumAtLeastByTeam(currentTeam.getName(), LOSE_SUM)) {
            currentTeam.setAlive(false);
            return MatchEvent.getTypeTeamDie(currentTeam);
        }
        return null;
    }

    /**
     * 答一题即切换（被调用则无条件切换）
     */
    @Override
    protected MatchEvent checkSwitchTeamEvent() {
        Team lastTeam = currentTeam;
        switchTeamAndNewQuestion();
        return MatchEvent.getTypeSwitchTeam(lastTeam, currentTeam);
    }

    /**
     * 固定加1分
     */
    @Override
    protected void addScore(boolean correct) {
        if (correct) {
            currentTeam.addScore(1);
        }
    }

    

}
