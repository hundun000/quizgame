package com.zaca.stillstanding.domain.match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.buff.BuffModel;
import com.zaca.stillstanding.domain.buff.BuffEffect;
import com.zaca.stillstanding.domain.buff.RunTimeBuff;
import com.zaca.stillstanding.domain.buff.ScoreComboBuffEffect;
import com.zaca.stillstanding.domain.buff.ScoreScaleBuffEffect;
import com.zaca.stillstanding.domain.dto.AnswerType;
import com.zaca.stillstanding.domain.dto.EventType;
import com.zaca.stillstanding.domain.dto.MatchEvent;
import com.zaca.stillstanding.domain.dto.MatchSituationDTO;
import com.zaca.stillstanding.domain.dto.MatchState;
import com.zaca.stillstanding.domain.dto.TeamRuntimeInfoDTO;
import com.zaca.stillstanding.domain.dto.event.AnswerResultEvent;
import com.zaca.stillstanding.domain.dto.event.FinishEvent;
import com.zaca.stillstanding.domain.dto.event.SkillResultEvent;
import com.zaca.stillstanding.domain.dto.event.StartMatchEvent;
import com.zaca.stillstanding.domain.dto.event.StubEvent;
import com.zaca.stillstanding.domain.dto.event.SwitchTeamEvent;
import com.zaca.stillstanding.domain.event.MatchEventFactory;
import com.zaca.stillstanding.domain.question.Question;
import com.zaca.stillstanding.domain.skill.BaseRole;
import com.zaca.stillstanding.domain.skill.BaseSkill;
import com.zaca.stillstanding.domain.skill.SkillSlot;
import com.zaca.stillstanding.domain.skill.effect.AddBuffSkillEffect;
import com.zaca.stillstanding.domain.skill.effect.ISkillEffect;
import com.zaca.stillstanding.domain.team.HealthType;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.exception.TeamDeadException;
import com.zaca.stillstanding.service.BuffService;
import com.zaca.stillstanding.service.GameService;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.RoleSkillService;
import com.zaca.stillstanding.service.TeamService;

public abstract class BaseMatch {
    
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    MatchState state;
    protected final String sessionId;
    protected final HealthType healthType;
    
    protected final QuestionService questionService;
    protected final TeamService teamService;
    protected final RoleSkillService roleSkillService;
    protected final BuffService buffService;
    
	protected List<Team> teams = new ArrayList<>();
	protected Team currentTeam;
	protected int currentTeamIndex;
	protected Question currentQuestion;
	
	protected AnswerRecorder recorder = new AnswerRecorder();
	
	//protected List<MatchEvent> events = new ArrayList<>();
	
	public AnswerResultEvent answerResultEvent;
	public SkillResultEvent skillResultEvent;
	public StubEvent stubEvent;
	public SwitchTeamEvent switchTeamEvent;
	public FinishEvent finishEvent;
	public StartMatchEvent startMatchEvent;
	
	public BaseMatch(
	        QuestionService questionService,
	        TeamService teamService,
	        RoleSkillService roleSkillService,
	        BuffService buffService,
	        HealthType healthType,
	        String sessionId
	        ) {
        this.questionService = questionService;
        this.teamService = teamService;
        this.roleSkillService = roleSkillService;
        this.buffService = buffService;
        // TODO 测试阶段固定
        //this.id = UUID.randomUUID().toString();
        this.sessionId = sessionId;
        this.healthType = healthType;
        this.state = MatchState.WAIT_START;
    }
	
	public void setTeamsByNames(List<String> teamNames) throws NotFoundException {
		this.teams.clear();
		for (String teamName : teamNames) {
		    teams.add(teamService.getTeam(teamName));
		}
		this.currentTeam = null;
	}
	

	public void start() throws StillStandingException {
        teams.forEach(team -> team.resetForMatch());
	    currentTeamIndex = teams.size() - 1;
	    switchToNextTeam();
	    eventsClear();
	    this.startMatchEvent = MatchEventFactory.getTypeStartTeam(currentTeamIndex, this.teams);
        //events.add(checkSwitchQuestionEvent());
	    this.state = MatchState.WAIT_GENERATE_QUESTION;
    }
	
	private void eventsClear() {
	    this.answerResultEvent = null;
	    this.skillResultEvent = null;
	    this.stubEvent = null;
	    this.switchTeamEvent = null;
	    this.finishEvent = null;
	    this.startMatchEvent = null;
    }

    protected abstract int calculateCurrentHealth();
	
	public void commandLineControl(String line) throws StillStandingException {
        List<String> args = Arrays.asList(line.split(" "));
        
        String action = args.get(0);
        String answerText;
        
        switch (action) {
        case "ans":
        case "answer":
            answerText = args.get(1);
            teamAnswer(answerText);
            break;
        default:
            break;
        }
        
    }
	
	
	public void teamUseSkill(String skillName) throws StillStandingException {
        eventsClear();
        this.skillResultEvent = generalUseSkill(skillName);  
	}
	
	public void nextQustion() throws StillStandingException {
        eventsClear();
        this.stubEvent = checkSwitchQuestionEvent();
        //events.add(checkSwitchQuestionEvent());  
        this.state = MatchState.WAIT_ANSWER;
    }
	
	private SkillResultEvent generalUseSkill(String skillName) throws StillStandingException {
	    SkillResultEvent newEvents; 
	    BaseRole role = roleSkillService.getRole(currentTeam.getRoleName());
	    BaseSkill skill = role.getSkill(skillName);
        
        boolean success = currentTeam.getRoleRunTimeData().useOnce(skillName);
        if (success) {
            newEvents = getSkillSuccessMatchEvent(currentTeam, skill);
            
            for (ISkillEffect skillEffect : skill.getBackendEffects()) {
                if (skillEffect instanceof AddBuffSkillEffect) {
                    AddBuffSkillEffect addBuffSkillEffect = (AddBuffSkillEffect) skillEffect;
                    RunTimeBuff buff = buffService.generateRunTimeBuff(addBuffSkillEffect.getBuffName(), addBuffSkillEffect.getDuration());
                    currentTeam.addBuff(buff);
                }
            }
            
        } else {
            newEvents = MatchEventFactory.getTypeSkillUseOut(currentTeam.getName(), role.getName(), skill);
        }
        
        return newEvents;
    }
	
	private SkillResultEvent getSkillSuccessMatchEvent(Team team, BaseSkill skill) throws StillStandingException {
	    int skillRemainTime = team.getRoleRunTimeData().getSkillRemainTimes().get(skill.getName());
	    return MatchEventFactory.getTypeSkillSuccess(team.getName(), team.getRoleName(), skill, skillRemainTime);
    }
	
//	private List<MatchEvent> teamAnswerSkip() throws StillStandingException {
//        return teamAnswer(Question.SKIP_ANSWER_TEXT);
//    }
	public void teamAnswerTimeout() throws StillStandingException {
	   eventsClear();
       generalAnswer(Question.TIMEOUT_ANSWER_TEXT);

    }
	
	public void teamAnswer(String answerText) throws StillStandingException {
	    eventsClear();
	    generalAnswer(answerText);
	    //events.addAll(newEvents);
	    this.state = MatchState.WAIT_GENERATE_QUESTION;
    }

    /**
	 * 返回并更新进events
	 * @param answer
	 * @return
	 * @throws StillStandingException
	 */
	private void generalAnswer(String answer) throws StillStandingException {
	    if (!currentTeam.isAlive()) {
	        throw new TeamDeadException(currentTeam.getName());
	    }
	    if (currentQuestion == null) {
            throw new StillStandingException("当前没有待回答的题目", -1);
        }
	    
	    
	    AnswerType answerType = currentQuestion.calculateAnswerType(answer);
	    logger.info("generalAnswer:: {} is {}", answer, answerType);
        // 1. 记录回答
		recorder.addRecord(currentTeam.getName(), answer, currentQuestion.getId(), answerType);
		// 2. 结算加分与生命值
		this.answerResultEvent = addScoreAndCountHealth(answerType);
        // 4.判断比赛结束
        FinishEvent finishEvent = checkFinishEvent();
        if (finishEvent == null) {
            // 5.判断换队
            this.switchTeamEvent = checkSwitchTeamEvent();
//            // 6.换题
//            newEvents.add(checkSwitchQuestionEvent());
            // 6 清空旧题
            currentQuestion = null;
            // 7.
            updateBuffsDuration(answerType);
        } else {
            this.finishEvent = finishEvent;
        }

	}

	
	private void updateBuffsDuration(AnswerType answerType) {
	    // 某些BuffEffect有特殊的修改Duration规则
	    for (RunTimeBuff buff : currentTeam.getBuffs()) {
            for (BuffEffect buffEffect : buff.getModel().getBuffEffects()) {
                if (buffEffect instanceof ScoreComboBuffEffect) {
                    if (answerType == AnswerType.CORRECT) {
                        // 本身每回合所有buff会减1层。为了使答对后最终加1层，则在此处加2层
                        buff.addDuration(2);
                    } else {
                        buff.clearDuration();
                    }
                }
            }
        }
	    
	    // 所有buff减少一层，然后清理已经没有层数的buff
        Iterator<RunTimeBuff> iterator = currentTeam.getBuffs().iterator();
        while(iterator.hasNext()) {
            RunTimeBuff buff = iterator.next();
            buff.minusOneDurationAndCheckMaxDuration();
            if (buff.getDuration() <= 0) {
                iterator.remove();
            }
        }
    }

	protected static final int DEFAULT_CORRECT_ANSWER_SCORE = 1;
    /**
	 * 为刚刚的答题加分。
	 * 可实现为：固定加分；连续答对comb加分...
	 * 
	 * 为刚刚的答题结算剩余健康值
	 * 可实现为：累计答n题死亡；连续答错n题死亡；累计答错n题死亡...
	 * @param answerType 
	 */
	protected AnswerResultEvent addScoreAndCountHealth(AnswerType answerType) {
        /*
         * 固定加1分
         */
        int addScore = 0;
        if (answerType == AnswerType.CORRECT) {
            addScore = DEFAULT_CORRECT_ANSWER_SCORE;
            addScore += calculateAddScoreSumOffsetByBuffs(answerType, addScore);
        }
        currentTeam.addScore(addScore);
        
        int currentHealth = calculateCurrentHealth();
        
        if (currentHealth == 0) {
            currentTeam.setAlive(false);
        }
        
        return MatchEventFactory.getTypeAnswerResult(answerType, addScore, currentTeam.getMatchScore(), healthType, currentHealth);
    }

	/**
	 * 计算所有buff引起的加分offset
	 * @param baseScore
	 * @return
	 */
	protected int calculateAddScoreSumOffsetByBuffs(AnswerType answerType, int baseScore) {
	    int sumOffset = 0;
        for (RunTimeBuff buff : currentTeam.getBuffs()) {
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
	abstract protected SwitchTeamEvent checkSwitchTeamEvent();

	
	/**
	 * 换题时可指定不同时间
	 * @return
	 * @throws StillStandingException 
	 */
	protected StubEvent checkSwitchQuestionEvent() throws StillStandingException {
	    boolean removeToDirty = this.healthType != HealthType.ENDLESS;
	    currentQuestion = questionService.getNewQuestionForTeam(this.sessionId, currentTeam, removeToDirty);
	    return MatchEventFactory.getTypeSwitchQuestion(15);
	}
	
	protected FinishEvent checkFinishEvent() {
	    boolean allDie = true;
	    for (Team team : teams) {
	        if (team.isAlive()) {
	            allDie = false;
	            break;
	        }
	    }
	    if (allDie) {
//	        JSONObject scores = new JSONObject();
//	        teams.forEach(item -> scores.put(item.getName(), item.getMatchScore()));
	        Map<String, Integer> scores = new HashMap<>(teams.size());
	        teams.forEach(item -> scores.put(item.getName(), item.getMatchScore()));
	        return MatchEventFactory.getTypeFinish(scores);
	    } else {
	        return null;
	    }
	}
	
	protected void switchToNextTeam() {
	    int nextTeamIndex;
	    int tryTimes = 0;
	    do {
	        tryTimes++;
            if (tryTimes > teams.size()) {
                throw new RuntimeException("试图在所有队伍死亡情况下换队");
            }
            
    		nextTeamIndex = currentTeamIndex + 1;
    		if (nextTeamIndex == teams.size()) {
    			nextTeamIndex = 0;
    		}
    		
    		currentTeam = teams.get(nextTeamIndex);
	    } while (!currentTeam.isAlive());
	    
		currentTeamIndex = nextTeamIndex;
		
	}
	
	// ===== normal getter =====
	
	public String getSessionId() {
        return sessionId;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public QuestionService getQuestionService() {
        return questionService;
    }
	
	public int getCurrentTeamIndex() {
        return currentTeamIndex;
    }

	
	// ===== quick getter =====
	
//	/**
//	 * 直接取出需要的类型
//	 * @param type
//	 * @return
//	 */
//	public MatchEvent getEventByType(EventType type) {
//        for (MatchEvent event : events) {
//            if (event.getType() == type) {
//                return event;
//            }
//        }
//        return null;
//    }
	
//	public boolean containsEventByType(EventType type) {
//        for (MatchEvent event : events) {
//            if (event.getType() == type) {
//                return true;
//            }
//        }
//        return false;
//    }

    public MatchSituationDTO toMatchSituationDTO() {
        MatchSituationDTO dto = new MatchSituationDTO();
        dto.setId(this.getSessionId());
        dto.setQuestion(this.currentQuestion != null ? this.currentQuestion.toQuestionDTO() : null);
        dto.setCurrentTeamIndex(this.currentTeamIndex);
        dto.setAnswerResultEvent(answerResultEvent);
        dto.setSkillResultEvent(skillResultEvent);
        dto.setStubEvent(stubEvent);
        dto.setSwitchTeamEvent(switchTeamEvent);
        dto.setFinishEvent(finishEvent);
        List<TeamRuntimeInfoDTO> teamRuntimeInfos = new ArrayList<>(this.teams.size());
        this.teams.forEach(team -> teamRuntimeInfos.add(team.toTeamRuntimeInfoDTO()));
        dto.setTeamRuntimeInfos(teamRuntimeInfos);
        dto.setState(state);
        return dto;
    }
	
}
