package com.zaca.stillstanding.domain.team;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;


public class Team {
	
	private final String name;
	private List<String> pickUpTags;
	private List<String> banTags;
	private List<Integer> historyScores;
	private int matchScore;
	
	private double hitPickUpRate;
	private static double DEFAULT_HIT_PICK_UP_RATE = 0.4;
	private static double HIT_PICK_UP_RATE_INCREASE_STEP = 0.05;
	
	public Team(String name) {
		this.name = name;
		this.historyScores = new ArrayList<>();
		this.matchScore = 0;
		resetHitPickUpRate();
	}
	
	public void setPickUpTags(List<String> pickUpTags) {
		this.pickUpTags = pickUpTags;
	}
	
	public void setBanTags(List<String> banTags) {
		this.banTags = banTags;
	}

	public void addScore(int addition) {
		matchScore += addition;
	}
	
	public double getHitPickUpRate() {
		return hitPickUpRate;
	}
	
	public boolean isPickUpAndNotBan(Set<String> questionTags) {
		for (String questionTag:questionTags) {
			if (pickUpTags.contains(questionTag) && !banTags.contains(questionTag)) {
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
	
	public void resetHitPickUpRate() {
		this.hitPickUpRate = DEFAULT_HIT_PICK_UP_RATE;
	}
	
	public void increaseHitPickUpRate() {
		this.hitPickUpRate = Math.min(hitPickUpRate + HIT_PICK_UP_RATE_INCREASE_STEP, 1);
	}
	
	public JSONObject getMatchData() {
		JSONObject data = new JSONObject();
		data.put("name", name);
		data.put("score", matchScore);
		return data;
	}
	
}
