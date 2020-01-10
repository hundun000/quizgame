package com.zaca.stillstanding.domain.match;

import java.awt.Event;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.event.EventType;
import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.question.AnswerType;
import com.zaca.stillstanding.domain.question.Question;
import com.zaca.stillstanding.domain.skill.BaseRole;
import com.zaca.stillstanding.domain.skill.BaseSkill;
import com.zaca.stillstanding.domain.skill.SkillSlot;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.exception.TeamDeadException;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.RoleSkillService;
import com.zaca.stillstanding.service.TeamService;

public abstract class BaseMatch {
    
    private static int currentId;
    
    protected final String id;
	
    protected final QuestionService questionService;
    protected final TeamService teamService;
    protected final RoleSkillService roleSkillService;
    
	protected List<Team> teams = new ArrayList<>();
	protected Team currentTeam;
	protected int currentTeamIndex;
	protected Question currentQuestion;
	
	protected AnswerRecorder recorder = new AnswerRecorder();
	
	protected List<MatchEvent> events = new ArrayList<>();
	
	public BaseMatch(
	        QuestionService questionService,
	        TeamService teamService,
	        RoleSkillService roleSkillService
	        ) {
        this.questionService = questionService;
        this.teamService = teamService;
        this.roleSkillService = roleSkillService;
        // TODO 测试阶段固定
        //this.id = UUID.randomUUID().toString();
        this.id = String.valueOf(currentId++);
    }
	
	public void setTeamsByNames(String... teamNames) throws NotFoundException {
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
	    events.clear();
	    events.add(MatchEvent.getTypeStartTeam(currentTeam));
        events.add(checkSwitchQuestionEvent());
    }
	
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
	    BaseRole role = roleSkillService.getRole(currentTeam.getRoleName());
	    BaseSkill skill = role.getSkill(skillName);
        
	    if (skill != null) {
	        boolean successful = currentTeam.getRoleRunTimeData().useOnce(skillName);
	        
	        MatchEvent event;
	        if (successful) {
	            event = getSkillSuccessMatchEvent(skill);
            } else {
                event = MatchEvent.getTypeSkillUseOut(currentTeam.getName(), skill);
            }
	        
	        events.clear();
	        events.add(event);
	    } else {
	        throw new NotFoundException("当前队伍的技能中", skillName);
	    }
    }
	
	private MatchEvent getSkillSuccessMatchEvent(BaseSkill skill) throws StillStandingException {
	    switch (skill.getName()) {
//        case "跳过":
//            List<MatchEvent> eventsAfterSkip = teamAnswerSkip();
//            // 重置events成员
//            
//            JSONArray array = JSONArray.parseArray(JSON.toJSONString(eventsAfterSkip));
//            return MatchEvent.getTypeSkillSuccess(currentTeam.getName(), skill, array);
        default:
            return MatchEvent.getTypeSkillSuccess(currentTeam.getName(), skill);
        }
    }
	
//	private List<MatchEvent> teamAnswerSkip() throws StillStandingException {
//        return teamAnswer(Question.SKIP_ANSWER_TEXT);
//    }
	public void teamAnswerTimeout() throws StillStandingException {
	   teamAnswer(Question.TIMEOUT_ANSWER_TEXT);
    }
	/**
	 * 返回并更新进events
	 * @param answer
	 * @return
	 * @throws StillStandingException
	 */
	public void teamAnswer(String answer) throws StillStandingException {
	    if (!currentTeam.isAlive()) {
	        throw new TeamDeadException(currentTeam.getName());
	    }
	    List<MatchEvent> tempEvents = new ArrayList<>(); 
	    
	    AnswerType answerType = currentQuestion.calculateAnswerType(answer);
        // 1. 记录回答
		recorder.addRecord(currentTeam.getName(), answer, currentQuestion.getId(), answerType);
		// 2. 结算加分
		tempEvents.add(addScore(answerType));
		// 3.判断队伍死亡
        tempEvents.add(checkTeamDieEvent());
        // 4.判断比赛结束
        MatchEvent finishEvent = checkFinishEvent();
        if (finishEvent == null) {
            tempEvents.add(finishEvent);
            // 5.判断换队
            tempEvents.add(checkSwitchTeamEvent());
            // 6.换题
            tempEvents.add(checkSwitchQuestionEvent());
        }
        
        this.events.clear();
        this.events = tempEvents.stream().filter(s -> s != null).collect(Collectors.toList());
	}

	
	/**
	 * 为刚刚的答题加分。
	 * 可实现为：固定加分；连续答对comb加分...
	 * @param answerType 
	 */
	abstract protected MatchEvent addScore(AnswerType answerType);
	
	/**
	 * 判断队伍是否死亡。
	 * 可实现为：累计答n题死亡；连续答错n题死亡；累计答错n题死亡...
	 * @return
	 */
	abstract protected MatchEvent checkTeamDieEvent();

	
	/**
	 * 判断是否切换队伍。
     * 可实现为：答完一题切换；答错才切换...
	 * @return
	 */
	abstract protected MatchEvent checkSwitchTeamEvent();

	
	/**
	 * 换题时可指定不同时间
	 * @return
	 */
	protected MatchEvent checkSwitchQuestionEvent() {
	    currentQuestion = questionService.getNewQuestionForTeam(currentTeam);
	    return MatchEvent.getTypeSwitchQuestion(15);
	}
	
	protected MatchEvent checkFinishEvent() {
	    boolean allDie = true;
	    for (Team team : teams) {
	        if (team.isAlive()) {
	            allDie = false;
	            break;
	        }
	    }
	    if (allDie) {
	        return MatchEvent.getTypeFinish();
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
	
	public String getId() {
        return id;
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
	
	public List<MatchEvent> getEvents() {
        return events;
    }
	
	/**
	 * 直接取出需要的类型
	 * @param type
	 * @return
	 */
	public MatchEvent getEventByType(EventType type) {
        for (MatchEvent event : events) {
            if (event.getType() == type) {
                return event;
            }
        }
        return null;
    }
	
	public boolean containsEventByType(EventType type) {
        for (MatchEvent event : events) {
            if (event.getType() == type) {
                return true;
            }
        }
        return false;
    }
	
}
