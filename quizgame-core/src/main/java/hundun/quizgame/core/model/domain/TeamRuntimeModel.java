package hundun.quizgame.core.model.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hundun.quizgame.core.exception.NotFoundException;
import hundun.quizgame.core.model.domain.buff.BuffRuntimeModel;
import hundun.quizgame.core.prototype.TeamPrototype;
import hundun.quizgame.core.view.buff.BuffRuntimeView;
import hundun.quizgame.core.view.team.TeamRuntimeView;
import lombok.Getter;


public class TeamRuntimeModel {
    
    private static double DEFAULT_HIT_PICK_RATE = 0.2;
    private static double HIT_PICK_RATE_INCREASE_STEP = 0.05;
	@Getter
    private TeamPrototype prototype;
	@Getter
	private RoleRuntimeModel roleRuntimeModel;
	
	private int matchScore;
	private int health;
	
	private List<BuffRuntimeModel> buffs = new ArrayList<>();
	@Getter
	private double hitPickRate;
	
	
	public TeamRuntimeModel(TeamPrototype prototype) {
		this.prototype = prototype;
		int currentHealth = -1;
	    this.matchScore = 0;
        setHealth(currentHealth);
        if (prototype.getRolePrototype() != null) {
            roleRuntimeModel = new RoleRuntimeModel(prototype.getRolePrototype());
        } else {
            roleRuntimeModel = null;
        }
        
        resetHitPickRate();
        buffs.clear();
	}
	
	public void addScore(int addition) {
		matchScore += addition;
	}

	
	public void resetHitPickRate() {
		this.hitPickRate = DEFAULT_HIT_PICK_RATE;
	}
	
	public void increaseHitPickRate() {
		this.hitPickRate = Math.min(hitPickRate + HIT_PICK_RATE_INCREASE_STEP, 1);
	}
	
	public String getName() {
        return prototype.getName();
    }

	
	public boolean isAlive() {
        return this.health > 0;
    }

    public String getRoleName() {
        return roleRuntimeModel != null ? roleRuntimeModel.getPrototype().getName() :null;
    }
    


    
    public int getMatchScore() {
        return matchScore;
    }
    
    public List<BuffRuntimeModel> getBuffs() {
        return buffs;
    }
    
    public void setHealth(int health) {
        this.health = health;
    }

	
	public void addBuff(BuffRuntimeModel newBuff) {
        for (BuffRuntimeModel buff : buffs) {
            if (buff.getPrototype().getName().equals(newBuff.getPrototype().getName())) {
                buff.addDuration(newBuff.getDuration());
                return;
            }
        }
        
        buffs.add(newBuff);
    }
	
	public SkillSlotRuntimeModel getSkillSlotRuntime(String skillName) throws NotFoundException {
	    if (roleRuntimeModel != null) {
	        for (SkillSlotRuntimeModel skillSlotRuntimeModel : roleRuntimeModel.getSkillSlotRuntimeModels()) {
	            if (skillSlotRuntimeModel.getPrototype().getName().equals(skillName)) {
	                return skillSlotRuntimeModel;
	            }
	        }
	    }
	    throw new NotFoundException("skill in team", skillName);
	}

    
    
    public TeamRuntimeView toTeamRuntimeInfoDTO() {
        TeamRuntimeView dto = new TeamRuntimeView();
        dto.setName(getName());
        dto.setMatchScore(matchScore);
        if (roleRuntimeModel != null) {
            dto.setRoleRuntimeInfo(roleRuntimeModel.toRoleRuntimeInfoDTO());
        }
        List<BuffRuntimeView> buffRuntimeViews = new ArrayList<>();
        buffs.forEach(item -> buffRuntimeViews.add(item.toRunTimeBuffDTO()));
        dto.setRuntimeBuffs(buffRuntimeViews);
        dto.setAlive(isAlive());
        dto.setHealth(health);
        return dto;
    }
    
    public boolean isPickAndNotBan(Set<String> questionTags) {
        for (String questionTag:questionTags) {
            if (prototype.getPickTags().contains(questionTag) && !prototype.getBanTags().contains(questionTag)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isNotBan(Set<String> questionTags) {
        for (String questionTag:questionTags) {
            if (prototype.getBanTags().contains(questionTag)) {
                return false;
            }
        }
        return true;
    }
    
}
