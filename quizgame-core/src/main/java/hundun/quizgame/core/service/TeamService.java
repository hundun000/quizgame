package hundun.quizgame.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hundun.quizgame.core.exception.NotFoundException;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.domain.TeamModel;
import hundun.quizgame.core.prototype.RolePrototype;
import hundun.quizgame.core.prototype.TeamPrototype;

@Service
public class TeamService {
    
    
    @Autowired
    RoleSkillService roleSkillServic;
    
	private Map<String, TeamPrototype> teamPrototypes = new HashMap<>();
	
	public void registerTeam(String teamName, List<String> pickTagNames, List<String> banTagNames, String roleName) throws QuizgameException {
        
	    RolePrototype rolePrototype = roleName != null ? roleSkillServic.getRole(roleName) : null;

	    TeamPrototype teamPrototype = new TeamPrototype(teamName, pickTagNames, banTagNames, rolePrototype);
	    
	    
	    teamPrototypes.put(teamName, teamPrototype); 


    }

	
	public void updateTeam(TeamPrototype teamPrototype) throws NotFoundException {
	    if (!existTeam(teamPrototype.getName())) {
            throw new NotFoundException(TeamModel.class.getSimpleName(), teamPrototype.getName());
        }
	    teamPrototypes.put(teamPrototype.getName(), teamPrototype);
    }
	
	
	
	
	
	public TeamPrototype getTeam(String name) throws NotFoundException {

		if (!existTeam(name)) {
			throw new NotFoundException(TeamModel.class.getSimpleName(), name);
		}
		return teamPrototypes.get(name);
	}
	
	public List<TeamPrototype> listTeams() {
        return new ArrayList<>(teamPrototypes.values());
    }
	
	public boolean existTeam(String name) {
        return teamPrototypes.containsKey(name);
    }

}
