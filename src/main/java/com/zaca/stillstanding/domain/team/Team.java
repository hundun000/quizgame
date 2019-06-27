package com.zaca.stillstanding.domain.team;

import java.util.List;


public class Team {
	
	private final String name;
	private List<String> pickUpTags;
	private List<String> banTags;
	
	public Team(String name) {
		this.name = name;
	}
	
	public void setPickUpTags(List<String> pickUpTags) {
		this.pickUpTags = pickUpTags;
	}
	
	public void setBanTags(List<String> banTags) {
		this.banTags = banTags;
	}

}
