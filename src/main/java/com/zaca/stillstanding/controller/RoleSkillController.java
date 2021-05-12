package com.zaca.stillstanding.controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zaca.stillstanding.core.role.BaseRole;
import com.zaca.stillstanding.service.RoleSkillService;

/**
 * @author hundun
 * Created on 2019/10/16
 */

@RestController
@RequestMapping("/api/roles")
public class RoleSkillController {
    
    private static final Logger logger = LoggerFactory.getLogger(RoleSkillController.class);
    
    
    @Autowired
    RoleSkillService roleSkillService;
    
    @RequestMapping(value="", method=RequestMethod.GET)
    public Object listTeams() {
        logger.info("===== \"\" =====");
        Collection<BaseRole> roles = roleSkillService.listRoles();
        return roles;
    }

}
