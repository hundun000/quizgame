package com.zaca.stillstanding.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.zaca.stillstanding.domain.question.TagManager;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.exception.ConflictException;
import com.zaca.stillstanding.exception.NotFoundException;

@Service
public class TeamService {
	
	private Map<String, Team> teams = new HashMap<>();
	
	public void creatTeam(String teamName) throws ConflictException {
		if (teams.containsKey(teamName)) {
			throw new ConflictException(Team.class.getSimpleName(), teamName);
		}
		Team team = new Team(teamName);
		teams.put(teamName, team);
	}
	
	
	public void setPickUpTagsForTeam(String teamName, List<String> tagNames) throws NotFoundException {
		setTagsForTeam(teamName, tagNames, true);
	}
	public void setBanTagsForTeam(String teamName, List<String> tagNames) throws NotFoundException {
		setTagsForTeam(teamName, tagNames, false);
	}
	private void setTagsForTeam(String teamName, List<String> tagNames, boolean isPickUp) throws NotFoundException {
		Team team = getTeam(teamName);
		for (String tagName:tagNames) {
			if (!TagManager.tagExsist(tagName)) {
				throw new NotFoundException("Tag", tagName);
			}
		}
		if (isPickUp) {
			team.setPickUpTags(tagNames);
		} else {
			team.setBanTags(tagNames);
		}
	}
	
	public Team getTeam(String name) throws NotFoundException {
		Team team = teams.get(name);
		if (team == null) {
			throw new NotFoundException(Team.class.getSimpleName(), name);
		}
		return team;
	}

}
