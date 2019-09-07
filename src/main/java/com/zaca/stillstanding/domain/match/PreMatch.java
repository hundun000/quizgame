package com.zaca.stillstanding.domain.match;

import org.springframework.stereotype.Component;

import com.zaca.stillstanding.domain.team.Team;

/**
 * @author hundun
 * Created on 2019/09/06
 */
@Component
public class PreMatch extends CoupleMatch {
    
    protected static final int LOSE_SUM = 5;
  
    
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
     * 一定不换队
     */
    @Override
    protected MatchEvent checkSwitchTeamEvent() {
        return null;
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
