package hundun.quizgame.core.model.domain.match;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hundun.quizgame.core.exception.NotFoundException;
import hundun.quizgame.core.exception.StateException;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.domain.Question;
import hundun.quizgame.core.model.domain.TeamRuntimeModel;
import hundun.quizgame.core.model.domain.buff.BuffRuntimeModel;
import hundun.quizgame.core.model.domain.buff.CombBuffStrategy;
import hundun.quizgame.core.model.domain.match.strategy.BaseMatchStrategy;
import hundun.quizgame.core.prototype.event.AnswerResultEvent;
import hundun.quizgame.core.prototype.event.FinishEvent;
import hundun.quizgame.core.prototype.event.SkillResultEvent;
import hundun.quizgame.core.prototype.event.StartMatchEvent;
import hundun.quizgame.core.prototype.event.SwitchQuestionEvent;
import hundun.quizgame.core.prototype.event.SwitchTeamEvent;
import hundun.quizgame.core.prototype.match.AnswerType;
import hundun.quizgame.core.prototype.match.ClientActionType;
import hundun.quizgame.core.prototype.match.MatchState;
import hundun.quizgame.core.service.MatchEventFactory;
import hundun.quizgame.core.tool.MatchStateUtils;
import hundun.quizgame.core.view.match.MatchSituationView;
import hundun.quizgame.core.view.team.TeamRuntimeView;


public class BaseMatch {
    
    MatchState state;
    protected final String sessionId;

	protected List<TeamRuntimeModel> teamRuntimeModels = new ArrayList<>();
	 
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
	
	public void initTeams(List<TeamRuntimeModel> teamRuntimeModels) throws NotFoundException {
		this.teamRuntimeModels.clear();
		this.teamRuntimeModels.addAll(teamRuntimeModels);
        for (TeamRuntimeModel teamRuntimeModel : teamRuntimeModels) {
            int currentHealth = strategy.calculateCurrentHealth();
            teamRuntimeModel.setHealth(currentHealth);
        }
		this.setCurrentTeam(-1);
	}
	

	public void start() throws QuizgameException {
	    checkStateException(state, ClientActionType.START_MATCH);
	    
	    setCurrentTeam(0);
 
	    eventsClear();
	    this.startMatchEvent = MatchEventFactory.getTypeStartMatch(this.teamRuntimeModels);
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

    
	
	
	
	
	public void teamUseSkill(String skillName) throws QuizgameException {
	    checkStateException(state, ClientActionType.USE_SKILL);
	    eventsClear();
        this.skillResultEvent = strategy.generalUseSkill(getCurrentTeam(), skillName);  
	}
	
	public void nextQustion() throws QuizgameException {
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
	
	
	
	
	

	public void teamAnswerTimeout() throws QuizgameException {
	   checkStateException(state, ClientActionType.ANSWER);
	   eventsClear();
       generalAnswer(Question.TIMEOUT_ANSWER_TEXT);
    }
	
	public void teamAnswer(String answerText) throws QuizgameException {
	    checkStateException(state, ClientActionType.ANSWER);
	    eventsClear();
	    generalAnswer(answerText);
    }

    /**
	 * 返回并更新进events
	 * @param answer
	 * @return
	 * @throws QuizgameException
	 */
	private void generalAnswer(String answer) throws QuizgameException {

	    AnswerType answerType = getCurrentQuestion().calculateAnswerType(answer);
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
	    for (BuffRuntimeModel buff : getCurrentTeam().getBuffs()) {
            if (buff.getBuffStrategy() != null) {
                
                if (answerType == AnswerType.CORRECT) {
                    // 本身每回合所有buff会减1层。为了使答对后最终加1层，则在此处加2层
                    buff.addDuration(2);
                } else {
                    buff.clearDuration();
                }
            }

        }
	    
	    // 所有buff减少一层，然后清理已经没有层数的buff
        Iterator<BuffRuntimeModel> iterator = getCurrentTeam().getBuffs().iterator();
        while(iterator.hasNext()) {
            BuffRuntimeModel buff = iterator.next();
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

    public List<TeamRuntimeModel> getTeams() {
        return teamRuntimeModels;
    }

    public TeamRuntimeModel getCurrentTeam() {
        if (currentTeamIndex >= 0) {
            return teamRuntimeModels.get(currentTeamIndex);
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

    public MatchSituationView toMatchSituationView() {
        MatchSituationView dto = new MatchSituationView();
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
        List<TeamRuntimeView> teamRuntimeInfos = new ArrayList<>(this.teamRuntimeModels.size());
        this.teamRuntimeModels.forEach(team -> teamRuntimeInfos.add(team.toTeamRuntimeInfoDTO()));
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
