package com.zaca.stillstanding.core.match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaca.stillstanding.core.buff.BuffRuntime;
import com.zaca.stillstanding.core.buff.effect.BuffEffect;
import com.zaca.stillstanding.core.buff.effect.ScoreComboBuffEffect;
import com.zaca.stillstanding.core.event.MatchEventFactory;
import com.zaca.stillstanding.core.match.strategy.BaseMatchStrategy;
import com.zaca.stillstanding.core.question.Question;
import com.zaca.stillstanding.core.team.TeamRuntime;
import com.zaca.stillstanding.dto.event.AnswerResultEvent;
import com.zaca.stillstanding.dto.event.FinishEvent;
import com.zaca.stillstanding.dto.event.SkillResultEvent;
import com.zaca.stillstanding.dto.event.StartMatchEvent;
import com.zaca.stillstanding.dto.event.SwitchQuestionEvent;
import com.zaca.stillstanding.dto.event.SwitchTeamEvent;
import com.zaca.stillstanding.dto.match.AnswerType;
import com.zaca.stillstanding.dto.match.ClientActionType;
import com.zaca.stillstanding.dto.match.MatchSituationDTO;
import com.zaca.stillstanding.dto.match.MatchState;
import com.zaca.stillstanding.dto.team.TeamRuntimeInfoDTO;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.exception.StateException;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.exception.TeamDeadException;
import com.zaca.stillstanding.service.GameService;
import com.zaca.stillstanding.tool.MatchStateUtils;

public class BaseMatch {
    
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    MatchState state;
    protected final String sessionId;

    
	protected List<TeamRuntime> teamRuntimes = new ArrayList<>();
	
	private int currentTeamIndex;
	private Question currentQuestion;
	
	protected AnswerRecorder recorder = new AnswerRecorder();

	
	public AnswerResultEvent answerResultEvent;
	public SkillResultEvent skillResultEvent;
	public SwitchQuestionEvent switchQuestionEvent;
	public SwitchTeamEvent switchTeamEvent;
	public FinishEvent finishEvent;
	public StartMatchEvent startMatchEvent;
	
	BaseMatchStrategy strategy;
	
	
	public BaseMatch(
	        String sessionId,
	        BaseMatchStrategy strategy
	        ) {

        // TODO 测试阶段固定
        //this.id = UUID.randomUUID().toString();
        this.sessionId = sessionId;
        this.strategy = strategy;
        this.state = MatchState.WAIT_START;
        
        
        strategy.initMatch(this);
    }
	
	public void initTeams(List<TeamRuntime> teamRuntimes) throws NotFoundException {
		this.teamRuntimes.clear();
		this.teamRuntimes.addAll(teamRuntimes);
		this.setCurrentTeam(-1);
	}
	

	public void start() throws StillStandingException {
	    checkStateException(state, ClientActionType.START_MATCH);
	    
	    setCurrentTeam(0);

        
        for (TeamRuntime teamRuntime : teamRuntimes) {
            int currentHealth = this.strategy.calculateCurrentHealth();
            teamRuntime.resetForMatch(currentHealth);
        }
        
	    eventsClear();
	    this.startMatchEvent = MatchEventFactory.getTypeStartMatch(this.teamRuntimes);
        //events.add(checkSwitchQuestionEvent());
	    this.state = MatchState.WAIT_GENERATE_QUESTION;
    }
	
	private void eventsClear() {
	    this.answerResultEvent = null;
	    this.skillResultEvent = null;
	    this.switchQuestionEvent = null;
	    this.switchTeamEvent = null;
	    this.finishEvent = null;
	    this.startMatchEvent = null;
    }

    
	
	
	
	
	public void teamUseSkill(String skillName) throws StillStandingException {
	    checkStateException(state, ClientActionType.USE_SKILL);
	    eventsClear();
        this.skillResultEvent = strategy.generalUseSkill(skillName);  
	}
	
	public void nextQustion() throws StillStandingException {
	    checkStateException(state, ClientActionType.NEXT_QUESTION);
        eventsClear();
        this.switchQuestionEvent = strategy.checkSwitchQuestionEvent(); 
        this.state = MatchState.WAIT_ANSWER;
    }
	
	
	private void checkStateException(MatchState state, ClientActionType actionType) throws StateException {
        if (!MatchStateUtils.check(state, actionType)) {
            throw new StateException(state.name(), actionType.name());
        }
    }
	
	
	
	
	
//	private List<MatchEvent> teamAnswerSkip() throws StillStandingException {
//        return teamAnswer(Question.SKIP_ANSWER_TEXT);
//    }
	public void teamAnswerTimeout() throws StillStandingException {
	   checkStateException(state, ClientActionType.ANSWER);
	   eventsClear();
       generalAnswer(Question.TIMEOUT_ANSWER_TEXT);
    }
	
	public void teamAnswer(String answerText) throws StillStandingException {
	    checkStateException(state, ClientActionType.ANSWER);
	    eventsClear();
	    generalAnswer(answerText);
    }

    /**
	 * 返回并更新进events
	 * @param answer
	 * @return
	 * @throws StillStandingException
	 */
	private void generalAnswer(String answer) throws StillStandingException {

	    AnswerType answerType = getCurrentQuestion().calculateAnswerType(answer);
	    logger.info("generalAnswer:: {} is {}", answer, answerType);
        // 记录回答
		getRecorder().addRecord(getCurrentTeam().getName(), answer, getCurrentQuestion().getId(), answerType);
		// 结算加分与生命值
		this.answerResultEvent = strategy.addScoreAndCountHealth(answerType);
		// 清空旧题
        setCurrentQuestion(null);
        // 结算buff
        updateBuffsDuration(answerType);
		// 判断比赛结束
		this.finishEvent = strategy.checkFinishEvent();
        if (finishEvent == null) {
            // 判断换队
            this.switchTeamEvent = strategy.checkSwitchTeamEvent();
        }
        
        this.state = MatchState.WAIT_GENERATE_QUESTION;
	}

	
	private void updateBuffsDuration(AnswerType answerType) {
	    // 某些BuffEffect有特殊的修改Duration规则
	    for (BuffRuntime buff : getCurrentTeam().getBuffs()) {
            for (BuffEffect buffEffect : buff.getPrototype().getBuffEffects()) {
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
        Iterator<BuffRuntime> iterator = getCurrentTeam().getBuffs().iterator();
        while(iterator.hasNext()) {
            BuffRuntime buff = iterator.next();
            buff.minusOneDurationAndCheckMaxDuration();
            if (buff.getDuration() <= 0) {
                iterator.remove();
            }
        }
    }

	
	
	// ===== normal getter =====
	
	public String getSessionId() {
        return sessionId;
    }

    public List<TeamRuntime> getTeams() {
        return teamRuntimes;
    }

    public TeamRuntime getCurrentTeam() {
        if (currentTeamIndex >= 0) {
            return teamRuntimes.get(currentTeamIndex);
        } else {
            return null;
        }
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
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
        dto.setQuestion(this.getCurrentQuestion() != null ? this.getCurrentQuestion().toQuestionDTO() : null);
        dto.setCurrentTeamIndex(this.getCurrentTeamIndex());
        dto.setCurrentTeamRuntimeInfo(getCurrentTeam() != null ? getCurrentTeam().toTeamRuntimeInfoDTO() : null);
        dto.setAnswerResultEvent(answerResultEvent);
        dto.setSkillResultEvent(skillResultEvent);
        dto.setStartMatchEvent(startMatchEvent);
        dto.setSwitchTeamEvent(switchTeamEvent);
        dto.setSwitchQuestionEvent(switchQuestionEvent);
        dto.setFinishEvent(finishEvent);
        List<TeamRuntimeInfoDTO> teamRuntimeInfos = new ArrayList<>(this.teamRuntimes.size());
        this.teamRuntimes.forEach(team -> teamRuntimeInfos.add(team.toTeamRuntimeInfoDTO()));
        dto.setTeamRuntimeInfos(teamRuntimeInfos);
        dto.setState(state);
        dto.setActionAdvices(MatchStateUtils.getValidClientActions(state));
        return dto;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }


    public void setCurrentTeam(int currentTeamIndex) {
        this.currentTeamIndex = currentTeamIndex;
        
    }

    public AnswerRecorder getRecorder() {
        return recorder;
    }
	
}
