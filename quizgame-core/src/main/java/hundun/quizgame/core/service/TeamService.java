package hundun.quizgame.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hundun.quizgame.core.context.IQuizCoreComponent;
import hundun.quizgame.core.context.QuizCoreContext;
import hundun.quizgame.core.exception.NotFoundException;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.domain.TeamRuntimeModel;
import hundun.quizgame.core.prototype.RolePrototype;
import hundun.quizgame.core.prototype.TeamPrototype;

public class TeamService implements IQuizCoreComponent {
    
    
    RoleSkillService roleSkillService;
    
	private Map<String, TeamPrototype> teamPrototypes = new HashMap<>();
	
	@Override
	public void wire(QuizCoreContext quizCoreContext) {
        this.roleSkillService = quizCoreContext.roleSkillService;
    }
	
	public void registerTeam(String teamName, List<String> pickTagNames, List<String> banTagNames, String roleName) throws QuizgameException {
        
	    RolePrototype rolePrototype = roleName != null ? roleSkillService.getRole(roleName) : null;

	    TeamPrototype teamPrototype = new TeamPrototype(teamName, pickTagNames, banTagNames, rolePrototype);
	    
	    
	    teamPrototypes.put(teamName, teamPrototype); 


    }

	
	public void updateTeam(TeamPrototype teamPrototype) throws NotFoundException {
	    if (!existTeam(teamPrototype.getName())) {
            throw new NotFoundException(TeamRuntimeModel.class.getSimpleName(), teamPrototype.getName());
        }
	    teamPrototypes.put(teamPrototype.getName(), teamPrototype);
    }
	
	
	
	
	
	public TeamPrototype getTeam(String name) throws NotFoundException {

		if (!existTeam(name)) {
			throw new NotFoundException(TeamRuntimeModel.class.getSimpleName(), name);
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
