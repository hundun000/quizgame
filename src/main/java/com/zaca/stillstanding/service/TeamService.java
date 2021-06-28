package com.zaca.stillstanding.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zaca.stillstanding.core.team.TeamPrototype;
import com.zaca.stillstanding.core.team.TeamRuntime;
import com.zaca.stillstanding.exception.ConflictException;
import com.zaca.stillstanding.exception.NotFoundException;
import com.zaca.stillstanding.exception.StillStandingException;

@Service
public class TeamService {
    public static String DEMO_TEAM_NAME = "游客";
    
    @Autowired
    RoleSkillService roleSkillServic;
    
	private Map<String, TeamPrototype> teamPrototypes = new HashMap<>();
	
	public void quickRegisterTeam(String teamName, List<String> pickTagNames, List<String> banTagNames, String roleName) throws StillStandingException {
        creatTeam(teamName); 

        setPickTagsForTeam(teamName, pickTagNames);

        setBanTagsForTeam(teamName, banTagNames);
        
        setRoleForTeam(teamName, roleName);
    }
	
	public void creatTeam(String teamName) throws ConflictException {
		if (existTeam(teamName)) {
			throw new ConflictException(TeamRuntime.class.getSimpleName(), teamName);
		}
		TeamPrototype teamRuntime = new TeamPrototype(teamName);
		teamPrototypes.put(teamName, teamRuntime);
	}
	
	public void updateTeam(TeamPrototype teamPrototype) throws NotFoundException {
	    if (!existTeam(teamPrototype.getName())) {
            throw new NotFoundException(TeamRuntime.class.getSimpleName(), teamPrototype.getName());
        }
	    teamPrototypes.put(teamPrototype.getName(), teamPrototype);
    }
	
	
	public void setPickTagsForTeam(String teamName, List<String> tagNames) throws NotFoundException {
		setTagsForTeam(teamName, tagNames, true);
	}
	public void setBanTagsForTeam(String teamName, List<String> tagNames) throws NotFoundException {
		setTagsForTeam(teamName, tagNames, false);
	}
	private void setTagsForTeam(String teamName, List<String> tagNames, boolean isPick) throws NotFoundException {
	    TeamPrototype teamRuntime = getTeam(teamName);
//		for (String tagName:tagNames) {
//			if (!TagManager.tagExsist(tagName)) {
//				throw new NotFoundException("Tag", tagName);
//			}
//		}
		if (isPick) {
			teamRuntime.setPickTags(tagNames);
		} else {
			teamRuntime.setBanTags(tagNames);
		}
	}
	
	public void setRoleForTeam(String teamName, String roleName) throws NotFoundException {
        if (roleName != null) {
            TeamPrototype teamRuntime = getTeam(teamName);
            
            if (!roleSkillServic.existRole(roleName)) {
                throw new NotFoundException("Role", roleName);
            }
            
            teamRuntime.setRolePrototype(roleSkillServic.getRole(roleName));
        }
    }
	
	
	public TeamPrototype getTeam(String name) throws NotFoundException {

		if (!existTeam(name)) {
			throw new NotFoundException(TeamRuntime.class.getSimpleName(), name);
		}
		return teamPrototypes.get(name);
	}
	
	public Collection<TeamPrototype> listTeams() {
        return teamPrototypes.values();
    }
	
	public boolean existTeam(String name) {
        return teamPrototypes.containsKey(name);
    }

}
