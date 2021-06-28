package com.zaca.stillstanding.core.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zaca.stillstanding.core.buff.BuffRuntime;
import com.zaca.stillstanding.core.role.RolePrototype;
import com.zaca.stillstanding.core.role.RoleRuntime;
import com.zaca.stillstanding.dto.buff.BuffRuntimeDTO;
import com.zaca.stillstanding.dto.team.TeamConstInfoDTO;
import com.zaca.stillstanding.dto.team.TeamRuntimeInfoDTO;

import lombok.Getter;


public class TeamRuntime {
    
    private static double DEFAULT_HIT_PICK_RATE = 0.2;
    private static double HIT_PICK_RATE_INCREASE_STEP = 0.05;
	@Getter
    private TeamPrototype prototype;
	@Getter
	private RoleRuntime roleRuntime;
	
	private int matchScore;
	private int health;
	
	private List<BuffRuntime> buffs = new ArrayList<>();
	
	private double hitPickRate;
	
	
	public TeamRuntime(TeamPrototype prototype) {
		this.prototype = prototype;
		resetForMatch(1);
	}
	
	public void resetForMatch(int currentHealth) {
	    this.matchScore = 0;
        setHealth(currentHealth);
        if (prototype.getRolePrototype() != null) {
            roleRuntime = new RoleRuntime(prototype.getRolePrototype());
        } else {
            roleRuntime = null;
        }
        
        resetHitPickRate();
        buffs.clear();
	}
	
	public void addScore(int addition) {
		matchScore += addition;
	}
	
	public double getHitPickRate() {
		return hitPickRate;
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
        return roleRuntime != null ? roleRuntime.getPrototype().getName() :null;
    }
    


    
    public int getMatchScore() {
        return matchScore;
    }
    
    public List<BuffRuntime> getBuffs() {
        return buffs;
    }
    
    public void setHealth(int health) {
        this.health = health;
    }

	
	public void addBuff(BuffRuntime newBuff) {
        for (BuffRuntime buff : buffs) {
            if (buff.getPrototype().getName().equals(newBuff.getPrototype().getName())) {
                buff.addDuration(newBuff.getDuration());
                return;
            }
        }
        
        buffs.add(newBuff);
    }

    
    
    public TeamRuntimeInfoDTO toTeamRuntimeInfoDTO() {
        TeamRuntimeInfoDTO dto = new TeamRuntimeInfoDTO();
        dto.setName(getName());
        dto.setMatchScore(matchScore);
        if (roleRuntime != null) {
            dto.setRoleRuntimeInfo(roleRuntime.toRoleRuntimeInfoDTO());
        }
        List<BuffRuntimeDTO> buffRuntimeDTOs = new ArrayList<>();
        buffs.forEach(item -> buffRuntimeDTOs.add(item.toRunTimeBuffDTO()));
        dto.setRuntimeBuffs(buffRuntimeDTOs);
        dto.setAlive(isAlive());
        dto.setHealth(health);
        return dto;
    }
    
}
