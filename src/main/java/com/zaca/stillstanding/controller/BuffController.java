package com.zaca.stillstanding.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.zaca.stillstanding.domain.buff.BuffModel;
import com.zaca.stillstanding.domain.dto.ApiResult;
import com.zaca.stillstanding.domain.dto.IApiResult;
import com.zaca.stillstanding.domain.team.Team;
import com.zaca.stillstanding.service.BuffService;

/**
 * @author hundun
 * Created on 2020/05/25
 */
@RestController
@RequestMapping("/api/buffs")
public class BuffController {
    
    @Autowired
    BuffService buffService;
    
//    @RequestMapping(value="", method=RequestMethod.GET)
//    public IApiResult listTeams() {
//        Collection<BuffModel> buffModels = buffService.listBuffModels();
//        JSONArray array = new JSONArray(buffModels.size());
//        buffModels.forEach(team -> array.add(team.toPayload()));
//        return new ApiResult(array);
//    }

}
