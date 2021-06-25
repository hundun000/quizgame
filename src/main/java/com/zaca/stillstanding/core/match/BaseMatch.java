package com.zaca.stillstanding.core.match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zaca.stillstanding.core.buff.BuffEffect;
import com.zaca.stillstanding.core.buff.RunTimeBuff;
import com.zaca.stillstanding.core.buff.ScoreComboBuffEffect;
import com.zaca.stillstanding.core.event.MatchEventFactory;
import com.zaca.stillstanding.core.match.strategy.BaseMatchStrategy;
import com.zaca.stillstanding.core.question.Question;
import com.zaca.stillstanding.core.team.Team;
import com.zaca.stillstanding.dto.event.AnswerResultEvent;
import com.zaca.stillstanding.dto.event.FinishEvent;
import com.zaca.stillstanding.dto.event.SkillResultEvent;
import com.zaca.stillstanding.dto.event.StartMatchEvent;
import com.zaca.stillstanding.dto.event.SwitchQuestionEvent;
import com.zaca.stillstanding.dto.event.SwitchTeamEvent;
import com.zaca.stillstanding.dto.match.AnswerType;
import com.zaca.stillstanding.dto.match.MatchSituationDTO;
import com.zaca.stillstanding.dto.match.MatchState;
import com.zaca.stillstanding.dto.team.TeamRuntimeInfoDTO;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.exception.TeamDeadException;
import com.zaca.stillstanding.service.GameService;

public class BaseMatch {
    
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    MatchState state;
    protected final String sessionId;

    
	protected List<Team> teams = new ArrayList<>();
	private Team currentTeam;
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
	
	public void initTeams(List<Team> teams) throws NotFoundException {
		this.teams.clear();
		this.teams.addAll(teams);
		this.setCurrentTeam(null);
	}
	

	public void start() throws StillStandingException {
	    setCurrentTeamIndex(0);
        setCurrentTeam(teams.get(0));
        
        for (Team team : teams) {
            int currentHealth = this.strategy.calculateCurrentHealth();
            team.resetForMatch(currentHealth);
        }
        
	    eventsClear();
	    this.startMatchEvent = MatchEventFactory.getTypeStartMatch(this.teams);
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
        eventsClear();
        this.skillResultEvent = strategy.generalUseSkill(skillName);  
	}
	
	public void nextQustion() throws StillStandingException {
        eventsClear();
        this.switchQuestionEvent = strategy.checkSwitchQuestionEvent();
        //events.add(checkSwitchQuestionEvent());  
        this.state = MatchState.WAIT_ANSWER;
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
	    if (!getCurrentTeam().isAlive()) {
	        throw new TeamDeadException(getCurrentTeam().getName());
	    }
	    if (getCurrentQuestion() == null) {
            throw new StillStandingException("当前没有待回答的题目", -1);
        }
	    
	    
	    AnswerType answerType = getCurrentQuestion().calculateAnswerType(answer);
	    logger.info("generalAnswer:: {} is {}", answer, answerType);
        // 1. 记录回答
		getRecorder().addRecord(getCurrentTeam().getName(), answer, getCurrentQuestion().getId(), answerType);
		// 2. 结算加分与生命值
		this.answerResultEvent = strategy.addScoreAndCountHealth(answerType);
        // 4.判断比赛结束
        FinishEvent finishEvent = strategy.checkFinishEvent();
        if (finishEvent == null) {
            // 5.判断换队
            this.switchTeamEvent = strategy.checkSwitchTeamEvent();
//            // 6.换题
//            newEvents.add(checkSwitchQuestionEvent());
            // 6 清空旧题
            setCurrentQuestion(null);
            // 7.
            updateBuffsDuration(answerType);
        } else {
            this.finishEvent = finishEvent;
        }

	}

	
	private void updateBuffsDuration(AnswerType answerType) {
	    // 某些BuffEffect有特殊的修改Duration规则
	    for (RunTimeBuff buff : getCurrentTeam().getBuffs()) {
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
        Iterator<RunTimeBuff> iterator = getCurrentTeam().getBuffs().iterator();
        while(iterator.hasNext()) {
            RunTimeBuff buff = iterator.next();
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

    public List<Team> getTeams() {
        return teams;
    }

    public Team getCurrentTeam() {
        return currentTeam;
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
        dto.setAnswerResultEvent(answerResultEvent);
        dto.setSkillResultEvent(skillResultEvent);
        dto.setStartMatchEvent(startMatchEvent);
        dto.setSwitchTeamEvent(switchTeamEvent);
        dto.setSwitchQuestionEvent(switchQuestionEvent);
        dto.setFinishEvent(finishEvent);
        List<TeamRuntimeInfoDTO> teamRuntimeInfos = new ArrayList<>(this.teams.size());
        this.teams.forEach(team -> teamRuntimeInfos.add(team.toTeamRuntimeInfoDTO()));
        dto.setTeamRuntimeInfos(teamRuntimeInfos);
        dto.setState(state);
        return dto;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public void setCurrentTeam(Team currentTeam) {
        this.currentTeam = currentTeam;
    }

    public void setCurrentTeamIndex(int currentTeamIndex) {
        this.currentTeamIndex = currentTeamIndex;
    }

    public AnswerRecorder getRecorder() {
        return recorder;
    }
	
}
