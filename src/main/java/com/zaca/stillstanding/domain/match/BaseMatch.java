package com.zaca.stillstanding.domain.match;

import java.awt.Event;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
	
    protected final QuestionService questionService;
    protected final TeamService teamService;
    protected final RoleSkillService roleSkillService;
    
	protected List<Team> teams = new ArrayList<>();
	protected Team currentTeam;
	protected int currentTeamIndex;
	protected Question currentQuestion;
	
	AnswerRecorder recorder = new AnswerRecorder();
	
	List<MatchEvent> events = new ArrayList<>();
	
	public BaseMatch(
	        QuestionService questionService,
	        TeamService teamService,
	        RoleSkillService roleSkillService
	        ) {
        this.questionService = questionService;
        this.teamService = teamService;
        this.roleSkillService = roleSkillService;
    }
	
	public void addTeams(String... teamNames) throws NotFoundException {
		this.teams.clear();
		for (String teamName : teamNames) {
		    teams.add(teamService.getTeam(teamName));
		}
		this.currentTeam = null;
	}
	
	public MatchEvent start() throws StillStandingException {
	    currentTeamIndex = teams.size() - 1;
	    switchToNextTeam();
	    return checkSwitchQuestionEvent();
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
	
	
	public MatchEvent teamUseSkill(String skillName) throws StillStandingException {
	    if (currentTeam.getRoleRunTimeData().getSkillCounters().containsKey(skillName)) {
	        boolean successful = currentTeam.getRoleRunTimeData().useOnce(skillName);
	        BaseRole role = roleSkillService.getRole(currentTeam.getRoleName());
	        BaseSkill skill = role.getSkillSlots().stream().filter(skillSlot -> skillSlot.getSkill().getName().equals(skillName)).findFirst().get().getSkill();
	        if (successful) {
                return handleSkillSuccess(skill);
            } else {
                return MatchEvent.getTypeSkillUseOut(currentTeam, skill);
            }
	    }
	    throw new NotFoundException("当前队伍的技能中", skillName);
    }
	
	private MatchEvent handleSkillSuccess(BaseSkill skill) throws StillStandingException {
	    events.clear();
	    switch (skill.getName()) {
        case "跳过":
            List<MatchEvent> eventsAfterSkip = teamAnswerSkip();
            JSONArray array = JSONArray.parseArray(JSON.toJSONString(eventsAfterSkip));
            return MatchEvent.getTypeSkillSuccess(currentTeam, skill, array);
        default:
            return MatchEvent.getTypeSkillSuccess(currentTeam, skill, null);
        }
    }
	
	public List<MatchEvent> teamAnswerSkip() throws StillStandingException {
        return teamAnswer(Question.SKIP_ANSWER_TEXT);
    }
	public List<MatchEvent> teamAnswerTimeout() throws StillStandingException {
	    return teamAnswer(Question.TIMEOUT_ANSWER_TEXT);
    }
	public List<MatchEvent> teamAnswer(String answer) throws StillStandingException {
	    events.clear();
	    if (!currentTeam.isAlive()) {
	        throw new TeamDeadException(currentTeam.getName());
	    }
	     
	    AnswerType answerType = currentQuestion.calculateAnswerType(answer);
        // 1. 记录回答
		recorder.addRecord(currentTeam.getName(), answer, currentQuestion.getId(), answerType);
		// 2. 结算加分
		addScore(answerType);
		// 3.判断队伍死亡
        events.add(checkTeamDieEvent());
        // 4.判断比赛结束
        MatchEvent finishEvent = checkFinishEvent();
        if (finishEvent == null) {
            events.add(finishEvent);
            // 5.判断换队
            events.add(checkSwitchTeamEvent());
            // 6.换题
            events.add(checkSwitchQuestionEvent());
        }
		// 移除空元素
		events = events.stream().filter(s -> s != null).collect(Collectors.toList());
		return events;
	}
	
	/**
	 * 为刚刚的答题加分。
	 * 可实现为：固定加分；连续答对comb加分...
	 * @param answerType 
	 */
	abstract protected void addScore(AnswerType answerType);
	
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
