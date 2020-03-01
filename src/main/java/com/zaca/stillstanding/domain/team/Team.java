package com.zaca.stillstanding.domain.team;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.zaca.stillstanding.domain.skill.BaseRole;
import com.zaca.stillstanding.domain.skill.RoleRunTimeData;
import com.zaca.stillstanding.domain.skill.SkillSlot;


public class Team {
	
	private final String name;
	private List<String> pickTags;
	private List<String> banTags;
	
	private RoleRunTimeData roleRunTimeData;
	
	private int matchScore;
	private BaseRole role;
	
	private double hitPickRate;
	private static double DEFAULT_HIT_PICK_RATE = 0.2;
	private static double HIT_PICK_RATE_INCREASE_STEP = 0.05;
	
	boolean alive;
	
	
	public Team(String name) {
		this.name = name;
		resetForMatch();
	}
	
	public void resetForMatch() {
	    this.matchScore = 0;
        this.alive = true;
        if (role != null) {
            this.roleRunTimeData = new RoleRunTimeData(role);
        }
        resetHitPickRate();
	}
	
	public void setPickTags(List<String> pickTags) {
		this.pickTags = pickTags;
	}
	
	public void setBanTags(List<String> banTags) {
		this.banTags = banTags;
	}

	public void addScore(int addition) {
		matchScore += addition;
	}
	
	public double getHitPickRate() {
		return hitPickRate;
	}
	
	public boolean isPickAndNotBan(Set<String> questionTags) {
		for (String questionTag:questionTags) {
			if (pickTags.contains(questionTag) && !banTags.contains(questionTag)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isNotBan(Set<String> questionTags) {
		for (String questionTag:questionTags) {
			if (banTags.contains(questionTag)) {
				return false;
			}
		}
		return true;
	}
	
	public void resetHitPickRate() {
		this.hitPickRate = DEFAULT_HIT_PICK_RATE;
	}
	
	public void increaseHitPickRate() {
		this.hitPickRate = Math.min(hitPickRate + HIT_PICK_RATE_INCREASE_STEP, 1);
	}
	
	public JSONObject getMatchData() {
		JSONObject data = new JSONObject();
		data.put("alive", alive);
		data.put("name", name);
		data.put("score", getMatchScore());
		data.put("roleRunTimeData", roleRunTimeData);
		return data;
	}
	
	public JSONObject getAllData() {
        JSONObject data = getMatchData();
        data.put("pickTags", pickTags);
        data.put("banTags", banTags);
        data.put("roleName", getRoleName());
        return data;
    }
	
	public String getName() {
        return name;
    }
	
	public void setAlive(boolean alive) {
        this.alive = alive;
    }
	
	public boolean isAlive() {
        return alive;
    }

    public String getRoleName() {
        return role.getName();
    }

    public void setRole(BaseRole role) {
        this.role = role;
        this.roleRunTimeData = new RoleRunTimeData(role);
    }

    public RoleRunTimeData getRoleRunTimeData() {
        return roleRunTimeData;
    }
    
    public int getMatchScore() {
        return matchScore;
    }
    
	
	
}
