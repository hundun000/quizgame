package hundun.quizgame.core.model.domain.match.strategy;

import java.util.HashMap;
import java.util.Map;

import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.domain.SkillSlotRuntimeModel;
import hundun.quizgame.core.model.domain.TeamRuntimeModel;
import hundun.quizgame.core.model.domain.buff.BuffRuntimeModel;
import hundun.quizgame.core.model.domain.buff.CombBuffStrategy;
import hundun.quizgame.core.model.domain.match.BaseMatch;
import hundun.quizgame.core.prototype.buff.BuffStrategyType;
import hundun.quizgame.core.prototype.event.AnswerResultEvent;
import hundun.quizgame.core.prototype.event.FinishEvent;
import hundun.quizgame.core.prototype.event.SkillResultEvent;
import hundun.quizgame.core.prototype.event.SwitchQuestionEvent;
import hundun.quizgame.core.prototype.event.SwitchTeamEvent;
import hundun.quizgame.core.prototype.match.AnswerType;
import hundun.quizgame.core.prototype.match.HealthType;
import hundun.quizgame.core.prototype.skill.AddBuffSkillEffect;
import hundun.quizgame.core.service.BuffService;
import hundun.quizgame.core.service.MatchEventFactory;
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
            addScore = calculateScoreAdditionByBuffs(answerType, addScore);
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
    protected int calculateScoreAdditionByBuffs(AnswerType answerType, int baseScoreAddition) {
        int modifiedScoreAddition = 0;
        for (BuffRuntimeModel buff : parent.getCurrentTeam().getBuffs()) {
            modifiedScoreAddition = buff.getBuffStrategy().modifyScore(baseScoreAddition, buff.getDuration());
        }
        return modifiedScoreAddition;
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
        for (TeamRuntimeModel teamRuntimeModel : parent.getTeams()) {
            if (!teamRuntimeModel.isAlive()) {
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
    
    
    
    public SkillResultEvent generalUseSkill(TeamRuntimeModel team, String skillName) throws QuizgameException {
        SkillResultEvent newEvents; 
        
        SkillSlotRuntimeModel skillSlotRuntimeModel = parent.getCurrentTeam().getSkillSlotRuntime(skillName);
        
        boolean success = skillSlotRuntimeModel.useOnce(skillName);
        if (success) {
            newEvents = MatchEventFactory.getTypeSkillSuccess(team.getName(), team.getRoleName(), skillSlotRuntimeModel);
                    
            for (AddBuffSkillEffect skillEffect : skillSlotRuntimeModel.getPrototype().getBackendEffects()) {
                AddBuffSkillEffect addBuffSkillEffect = skillEffect;
                BuffRuntimeModel buff = buffService.generateRunTimeBuff(addBuffSkillEffect.getBuffName(), addBuffSkillEffect.getDuration());
                parent.getCurrentTeam().addBuff(buff);
            }
            
        } else {
            newEvents = MatchEventFactory.getTypeSkillUseOut(team.getName(), team.getRoleName(), skillSlotRuntimeModel);
        }
        
        return newEvents;
    }
    


    public void initMatch(BaseMatch baseMatch) {
        this.parent = baseMatch;
    }
    
    
}
