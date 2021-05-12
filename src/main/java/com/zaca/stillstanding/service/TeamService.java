package com.zaca.stillstanding.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zaca.stillstanding.core.team.Team;
import com.zaca.stillstanding.exception.ConflictException;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.exception.StillStandingException;

@Service
public class TeamService {
    
    @Autowired
    RoleSkillService roleSkillServic;
    
	private Map<String, Team> teams = new HashMap<>();
	
	public void quickRegisterTeam(String teamName, List<String> pickTagNames, List<String> banTagNames, String roleName) throws StillStandingException {
        creatTeam(teamName); 

        setPickTagsForTeam(teamName, pickTagNames);

        setBanTagsForTeam(teamName, banTagNames);
        
        setRoleForTeam(teamName, roleName);
    }
	
	public void creatTeam(String teamName) throws ConflictException {
		if (existTeam(teamName)) {
			throw new ConflictException(Team.class.getSimpleName(), teamName);
		}
		Team team = new Team(teamName);
		teams.put(teamName, team);
	}
	
	public void updateTeam(Team team) throws NotFoundException {
	    if (!existTeam(team.getName())) {
            throw new NotFoundException(Team.class.getSimpleName(), team.getName());
        }
	    teams.put(team.getName(), team);
    }
	
	
	public void setPickTagsForTeam(String teamName, List<String> tagNames) throws NotFoundException {
		setTagsForTeam(teamName, tagNames, true);
	}
	public void setBanTagsForTeam(String teamName, List<String> tagNames) throws NotFoundException {
		setTagsForTeam(teamName, tagNames, false);
	}
	private void setTagsForTeam(String teamName, List<String> tagNames, boolean isPick) throws NotFoundException {
		Team team = getTeam(teamName);
//		for (String tagName:tagNames) {
//			if (!TagManager.tagExsist(tagName)) {
//				throw new NotFoundException("Tag", tagName);
//			}
//		}
		if (isPick) {
			team.setPickTags(tagNames);
		} else {
			team.setBanTags(tagNames);
		}
	}
	
	public void setRoleForTeam(String teamName, String roleName) throws NotFoundException {
        
	    Team team = getTeam(teamName);
	    
        if (!roleSkillServic.existRole(roleName)) {
            throw new NotFoundException("Role", roleName);
        }
        
        team.setRole(roleSkillServic.getRole(roleName));
    }
	
	
	public Team getTeam(String name) throws NotFoundException {

		if (!existTeam(name)) {
			throw new NotFoundException(Team.class.getSimpleName(), name);
		}
		return teams.get(name);
	}
	
	public Collection<Team> listTeams() {
        return teams.values();
    }
	
	public boolean existTeam(String name) {
        return teams.containsKey(name);
    }

}
