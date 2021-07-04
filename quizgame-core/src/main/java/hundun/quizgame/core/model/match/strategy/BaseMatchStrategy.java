package hundun.quizgame.core.model.match.strategy;

import java.util.HashMap;
import java.util.Map;

import hundun.quizgame.core.dto.event.AnswerResultEvent;
import hundun.quizgame.core.dto.event.FinishEvent;
import hundun.quizgame.core.dto.event.SkillResultEvent;
import hundun.quizgame.core.dto.event.SwitchQuestionEvent;
import hundun.quizgame.core.dto.event.SwitchTeamEvent;
import hundun.quizgame.core.dto.match.AnswerType;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.buff.BuffRuntime;
import hundun.quizgame.core.model.buff.effect.BuffEffect;
import hundun.quizgame.core.model.buff.effect.ScoreComboBuffEffect;
import hundun.quizgame.core.model.buff.effect.ScoreScaleBuffEffect;
import hundun.quizgame.core.model.event.MatchEventFactory;
import hundun.quizgame.core.model.match.BaseMatch;
import hundun.quizgame.core.model.role.RolePrototype;
import hundun.quizgame.core.model.skill.SkillConstData;
import hundun.quizgame.core.model.skill.effect.AddBuffSkillEffect;
import hundun.quizgame.core.model.skill.effect.ISkillEffect;
import hundun.quizgame.core.model.team.HealthType;
import hundun.quizgame.core.model.team.TeamRuntime;
import hundun.quizgame.core.service.BuffService;
import hundun.quizgame.core.service.QuestionService;
import hundun.quizgame.core.service.RoleSkillService;
import hundun.quizgame.core.service.TeamService;

/**
 * @author hundun
 * Created on 2021/05/11
 */
public abstract class BaseMatchStrategy {
    
    BaseMatch parent;
    
    protected QuestionService questionService;
    protected TeamService teamService;
    protected RoleSkillService roleSkillService;
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
        for (BuffRuntime buff : parent.getCurrentTeam().getBuffs()) {
            for (BuffEffect buffEffect : buff.getPrototype().getBuffEffects()) {
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
     * @throws QuizgameException 
     */
    public SwitchQuestionEvent checkSwitchQuestionEvent() throws QuizgameException {
        boolean removeToDirty = this.healthType != HealthType.ENDLESS;
        parent.setCurrentQuestion(questionService.getNewQuestionForTeam(parent.getSessionId(), parent.getCurrentTeam(), removeToDirty));
        return MatchEventFactory.getTypeSwitchQuestion(15);
    }
    
    public FinishEvent checkFinishEvent() {
        boolean anyDie = false;
        for (TeamRuntime teamRuntime : parent.getTeams()) {
            if (!teamRuntime.isAlive()) {
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
        int checkingIndex = parent.getCurrentTeamIndex();
        int tryTimes = 0;
        
        do {
            tryTimes++;
            if (tryTimes > parent.getTeams().size()) {
                throw new RuntimeException("试图在所有队伍死亡情况下换队");
            }
            
            checkingIndex++;
            if (checkingIndex == parent.getTeams().size()) {
                checkingIndex = 0;
            }
            
        } while (!parent.getTeams().get(checkingIndex).isAlive());
        
        parent.setCurrentTeam(checkingIndex);
        
    }
    
    public abstract int calculateCurrentHealth();
    
    public SkillResultEvent generalUseSkill(String skillName) throws QuizgameException {
        SkillResultEvent newEvents; 
        RolePrototype role = roleSkillService.getRole(parent.getCurrentTeam().getRoleName());
        SkillConstData skill = role.getSkill(skillName);
        
        boolean success = parent.getCurrentTeam().getRoleRuntime().useOnce(skillName);
        if (success) {
            newEvents = getSkillSuccessMatchEvent(parent.getCurrentTeam(), skill);
            
            for (ISkillEffect skillEffect : skill.getBackendEffects()) {
                if (skillEffect instanceof AddBuffSkillEffect) {
                    AddBuffSkillEffect addBuffSkillEffect = (AddBuffSkillEffect) skillEffect;
                    BuffRuntime buff = buffService.generateRunTimeBuff(addBuffSkillEffect.getBuffName(), addBuffSkillEffect.getDuration());
                    parent.getCurrentTeam().addBuff(buff);
                }
            }
            
        } else {
            newEvents = MatchEventFactory.getTypeSkillUseOut(parent.getCurrentTeam().getName(), role.getName(), skill);
        }
        
        return newEvents;
    }
    
    
    
    private SkillResultEvent getSkillSuccessMatchEvent(TeamRuntime teamRuntime, SkillConstData skill) throws QuizgameException {
        int skillRemainTime = teamRuntime.getRoleRuntime().getSkillRemainTimes().get(skill.getName());
        return MatchEventFactory.getTypeSkillSuccess(teamRuntime.getName(), teamRuntime.getRoleName(), skill, skillRemainTime);
    }

    public void initMatch(BaseMatch baseMatch) {
        this.parent = baseMatch;
    }
}
