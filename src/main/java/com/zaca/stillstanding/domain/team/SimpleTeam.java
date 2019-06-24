package com.zaca.stillstanding.domain.team;

import java.util.List;

import com.zaca.stillstanding.domain.tag.Tag;

public class SimpleTeam {
	
	private final String name;
	private List<Tag> pickUpTags;
	private List<Tag> banTags;
	
	public SimpleTeam(String name) {
		this.name = name;
	}

}
