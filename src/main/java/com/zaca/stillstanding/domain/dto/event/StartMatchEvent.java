package com.zaca.stillstanding.domain.dto.event;

import java.util.List;

import com.zaca.stillstanding.domain.dto.MatchEvent;
import com.zaca.stillstanding.domain.dto.TeamDTO;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class StartMatchEvent extends MatchEvent {
    List<TeamDTO> teamDTOs;
}
