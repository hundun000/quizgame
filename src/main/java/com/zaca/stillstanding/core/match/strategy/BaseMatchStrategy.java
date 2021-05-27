package com.zaca.stillstanding.core.match.strategy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaca.stillstanding.core.buff.BuffEffect;
import com.zaca.stillstanding.core.buff.RunTimeBuff;
import com.zaca.stillstanding.core.buff.ScoreComboBuffEffect;
import com.zaca.stillstanding.core.buff.ScoreScaleBuffEffect;
import com.zaca.stillstanding.core.event.MatchEventFactory;
import com.zaca.stillstanding.core.match.BaseMatch;
import com.zaca.stillstanding.core.role.RoleConstData;
import com.zaca.stillstanding.core.skill.SkillConstData;
import com.zaca.stillstanding.core.skill.effect.AddBuffSkillEffect;
import com.zaca.stillstanding.core.skill.effect.ISkillEffect;
import com.zaca.stillstanding.core.team.HealthType;
import com.zaca.stillstanding.core.team.Team;
import com.zaca.stillstanding.dto.event.AnswerResultEvent;
import com.zaca.stillstanding.dto.event.FinishEvent;
import com.zaca.stillstanding.dto.event.SkillResultEvent;

import com.zaca.stillstanding.dto.event.SwitchQuestionEvent;
import com.zaca.stillstanding.dto.event.SwitchTeamEvent;
import com.zaca.stillstanding.dto.match.AnswerType;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.service.BuffService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.RoleSkillService;
import com.zaca.stillstanding.service.TeamService;

/**
 * @author hundun
 * Created on 2021/05/11
 */
@Component
public abstract class BaseMatchStrategy {
    
    BaseMatch parent;
    
    @Autowired
    protected QuestionService questionService;
    @Autowired
    protected TeamService teamService;
    @Autowired
    protected RoleSkillService roleSkillService;
    @Autowired
    protected BuffService buffService;
    
    protected final HealthType healthType;
    
    protected static final int DEFAULT_CORRECT_ANSWER_SCORE = 1;
    
    
    public BaseMatchStrategy(
            QuestionService questionService,
            TeamService teamService,
            RoleSkillService roleSkillService,
            BuffService buffService,
            HealthType healthType
            ) {
        this.questionService = questionService;
        this.teamService = teamService;
        this.roleSkillService = roleSkillService;
        this.buffService = buffService;
        this.healthType = healthType;
    }
    
    /**
     * 为刚刚的答题加分。
     * 可实现为：固定加分；连续答对comb加分...
     * 
     * 为刚刚的答题结算剩余健康值
     * 可实现为：累计答n题死亡；连续答错n题死亡；累计答错n题死亡...
     * @param answerType 
     */
    public AnswerResultEvent addScoreAndCountHealth(AnswerType answerType) {
        /*
         * 固定加1分
         */
        int addScore = 0;
        if (answerType == AnswerType.CORRECT) {
            addScore = DEFAULT_CORRECT_ANSWER_SCORE;
            addScore += calculateAddScoreSumOffsetByBuffs(answerType, addScore);
        }
        parent.getCurrentTeam().addScore(addScore);
        
        int currentHealth = calculateCurrentHealth();
        
        parent.getCurrentTeam().setHealth(currentHealth);
        
        
        return MatchEventFactory.getTypeAnswerResult(answerType, addScore, parent.getCurrentTeam().getName());
    }

    /**
     * 计算所有buff引起的加分offset
     * @param baseScore
     * @return
     */
    protected int calculateAddScoreSumOffsetByBuffs(AnswerType answerType, int baseScore) {
        int sumOffset = 0;
        for (RunTimeBuff buff : parent.getCurrentTeam().getBuffs()) {
            for (BuffEffect buffEffect : buff.getModel().getBuffEffects()) {
                if (buffEffect instanceof ScoreScaleBuffEffect) {
                    ScoreScaleBuffEffect scoreScaleBuffEffect = (ScoreScaleBuffEffect) buffEffect;
                    sumOffset += scoreScaleBuffEffect.getScoreOffset(baseScore);
                } else if (buffEffect instanceof ScoreComboBuffEffect) {
                    ScoreComboBuffEffect scoreScaleBuffEffect = (ScoreComboBuffEffect) buffEffect;
                    sumOffset += scoreScaleBuffEffect.getScoreOffset(buff.getDuration());
                }
            }
        }
        return sumOffset;
    }
    
    
    /**
     * 判断是否切换队伍。
     * 可实现为：答完一题切换；答错才切换...
     * @return
     */
    public abstract SwitchTeamEvent checkSwitchTeamEvent();

    
    /**
     * 换题时可指定不同时间
     * @return
     * @throws StillStandingException 
     */
    public SwitchQuestionEvent checkSwitchQuestionEvent() throws StillStandingException {
        boolean removeToDirty = this.healthType != HealthType.ENDLESS;
        parent.setCurrentQuestion(questionService.getNewQuestionForTeam(parent.getSessionId(), parent.getCurrentTeam(), removeToDirty));
        return MatchEventFactory.getTypeSwitchQuestion(15);
    }
    
    public FinishEvent checkFinishEvent() {
        boolean anyDie = false;
        for (Team team : parent.getTeams()) {
            if (!team.isAlive()) {
                anyDie = true;
                break;
            }
        }
        if (anyDie) {

            Map<String, Integer> scores = new HashMap<>(parent.getTeams().size());
            parent.getTeams().forEach(item -> scores.put(item.getName(), item.getMatchScore()));
            return MatchEventFactory.getTypeFinish(scores);
        } else {
            return null;
        }
    }
    
    public void switchToNextTeam() {
        int nextTeamIndex;
        int tryTimes = 0;
        do {
            tryTimes++;
            if (tryTimes > parent.getTeams().size()) {
                throw new RuntimeException("试图在所有队伍死亡情况下换队");
            }
            
            nextTeamIndex = parent.getCurrentTeamIndex() + 1;
            if (nextTeamIndex == parent.getTeams().size()) {
                nextTeamIndex = 0;
            }
            
            parent.setCurrentTeam(parent.getTeams().get(nextTeamIndex));
        } while (!parent.getCurrentTeam().isAlive());
        
        parent.setCurrentTeamIndex(nextTeamIndex);
        
    }
    
    public abstract int calculateCurrentHealth();
    
    public SkillResultEvent generalUseSkill(String skillName) throws StillStandingException {
        SkillResultEvent newEvents; 
        RoleConstData role = roleSkillService.getRole(parent.getCurrentTeam().getRoleName());
        SkillConstData skill = role.getSkill(skillName);
        
        boolean success = parent.getCurrentTeam().getRoleRunTimeData().useOnce(skillName);
        if (success) {
            newEvents = getSkillSuccessMatchEvent(parent.getCurrentTeam(), skill);
            
            for (ISkillEffect skillEffect : skill.getBackendEffects()) {
                if (skillEffect instanceof AddBuffSkillEffect) {
                    AddBuffSkillEffect addBuffSkillEffect = (AddBuffSkillEffect) skillEffect;
                    RunTimeBuff buff = buffService.generateRunTimeBuff(addBuffSkillEffect.getBuffName(), addBuffSkillEffect.getDuration());
                    parent.getCurrentTeam().addBuff(buff);
                }
            }
            
        } else {
            newEvents = MatchEventFactory.getTypeSkillUseOut(parent.getCurrentTeam().getName(), role.getName(), skill);
        }
        
        return newEvents;
    }
    
    
    
    private SkillResultEvent getSkillSuccessMatchEvent(Team team, SkillConstData skill) throws StillStandingException {
        int skillRemainTime = team.getRoleRunTimeData().getSkillRemainTimes().get(skill.getName());
        return MatchEventFactory.getTypeSkillSuccess(team.getName(), team.getRoleName(), skill, skillRemainTime);
    }

    public void initMatch(BaseMatch baseMatch) {
        this.parent = baseMatch;
    }
}
