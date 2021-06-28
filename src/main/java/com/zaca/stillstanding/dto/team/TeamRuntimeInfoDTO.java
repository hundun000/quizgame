package com.zaca.stillstanding.dto.team;

import java.util.List;
import java.util.Map;

import com.zaca.stillstanding.dto.buff.BuffRuntimeDTO;
import com.zaca.stillstanding.dto.role.RoleRuntimeInfoDTO;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class TeamRuntimeInfoDTO {
    String name;
    int matchScore;
    RoleRuntimeInfoDTO roleRuntimeInfo;
    List<BuffRuntimeDTO> runtimeBuffs;
    boolean alive;
    int health;
}
