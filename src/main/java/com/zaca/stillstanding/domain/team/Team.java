package com.zaca.stillstanding.domain.team;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;


public class Team {
	
	private final String name;
	private List<String> pickTags;
	private List<String> banTags;
	
	private int matchScore;
	
	private double hitPickRate;
	private static double DEFAULT_HIT_PICK_RATE = 0.4;
	private static double HIT_PICK_RATE_INCREASE_STEP = 0.05;
	
	boolean alive;
	
	
	public Team(String name) {
		this.name = name;
		this.matchScore = 0;
		this.alive = true;
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
		data.put("score", matchScore);
		return data;
	}
	
	public String getName() {
        return name;
    }
	
	public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
