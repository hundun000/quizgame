package com.zaca.stillstanding.domain.match;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaca.stillstanding.domain.question.AnswerType;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.ConflictException;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.TeamService;

/**
 * @author hundun
 * Created on 2019/09/06
 */
@Component
public class PreMatch extends BaseMatch {
    
    
    
    public void initForTest() throws Exception {
        String  teamKancolle = "砍口垒同好组";
        
        teamService.creatTeam(teamKancolle); 
        List<String> pickTagNames = new ArrayList<>();
        pickTagNames.add("单机游戏");
        teamService.setPickTagsForTeam(teamKancolle, pickTagNames);
        
        List<String> banTagNames = new ArrayList<>();
        banTagNames.add("动画");
        teamService.setBanTagsForTeam(teamKancolle, banTagNames);
        
        init(teamKancolle);
    }
    
    protected static final int LOSE_SUM = 3;
  
    
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
    protected void addScore(AnswerType answerType) {
        if (answerType == AnswerType.CORRECT) {
            currentTeam.addScore(1);
        }
    }

    

}
