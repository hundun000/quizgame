package com.zaca.stillstanding.exception;
/**
 * @author hundun
 * Created on 2019/10/12
 */
public class TeamDeadException extends StillStandingException {
    
    public TeamDeadException(String teamName) {
        super(teamName + "已经死了");
    }

}
