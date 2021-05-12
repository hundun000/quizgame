package com.zaca.stillstanding.core.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zaca.stillstanding.core.buff.RunTimeBuff;
import com.zaca.stillstanding.core.role.BaseRole;
import com.zaca.stillstanding.core.role.RoleRuntimeData;
import com.zaca.stillstanding.dto.team.TeamConstInfoDTO;
import com.zaca.stillstanding.dto.team.TeamRuntimeInfoDTO;


public class Team {
    
    private static double DEFAULT_HIT_PICK_RATE = 0.2;
    private static double HIT_PICK_RATE_INCREASE_STEP = 0.05;
	
	private final String name;
	private List<String> pickTags;
	private List<String> banTags;
	private BaseRole role;
	
	private RoleRuntimeData roleRuntimeData;
	
	private int matchScore;
	boolean alive;
	private List<RunTimeBuff> buffs = new ArrayList<>();
	
	private double hitPickRate;
	
	
	public Team(String name) {
		this.name = name;
		resetForMatch();
	}
	
	public void resetForMatch() {
	    this.matchScore = 0;
        this.alive = true;
        if (role != null) {
            this.roleRuntimeData = new RoleRuntimeData(role);
        }
        resetHitPickRate();
        buffs.clear();
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
	
	public String getName() {
        return name;
    }
	
	public void setAlive(boolean alive) {
        this.alive = alive;
    }
	
	public boolean isAlive() {
        return alive;
    }

    public String getRoleName() {
        return role.getName();
    }
    
    public BaseRole getRole() {
        return role;
    }

    public void setRole(BaseRole role) {
        this.role = role;
        this.roleRuntimeData = new RoleRuntimeData(role);
    }

    public RoleRuntimeData getRoleRunTimeData() {
        return roleRuntimeData;
    }
    
    public int getMatchScore() {
        return matchScore;
    }
    
    public List<RunTimeBuff> getBuffs() {
        return buffs;
    }
	
	public void addBuff(RunTimeBuff newBuff) {
        for (RunTimeBuff buff : buffs) {
            if (buff.getModel().getName().equals(newBuff.getModel().getName())) {
                buff.addDuration(newBuff.getDuration());
                return;
            }
        }
        
        buffs.add(newBuff);
    }

    public TeamConstInfoDTO toTeamDTO() {
        TeamConstInfoDTO teamConstInfoDTO = new TeamConstInfoDTO();
        teamConstInfoDTO.setName(this.getName());
        teamConstInfoDTO.setPickTags(this.pickTags);
        teamConstInfoDTO.setBanTags(this.banTags);
        teamConstInfoDTO.setRoleName(this.role != null ? role.getName() : null);
        return teamConstInfoDTO;
    }
    
    public TeamRuntimeInfoDTO toTeamRuntimeInfoDTO() {
        TeamRuntimeInfoDTO dto = new TeamRuntimeInfoDTO();
        dto.setName(name);
        dto.setRoleName(role.getName());
        dto.setMatchScore(matchScore);
        dto.setSkillRemainTimes(roleRuntimeData.getSkillRemainTimes());
        Map<String, Integer> buffsData = new HashMap<>();
        buffs.forEach(item -> buffsData.put(item.getModel().getName(), item.getDuration()));
        dto.setBuffs(buffsData);
        dto.setAlive(alive);
        return dto;
    }
}