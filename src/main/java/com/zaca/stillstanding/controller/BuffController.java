package com.zaca.stillstanding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
