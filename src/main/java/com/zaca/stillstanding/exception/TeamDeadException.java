package com.zaca.stillstanding.exception;
/**
 * @author hundun
 * Created on 2019/10/12
 */
public class TeamDeadException extends StillStandingException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 4825087489852106495L;

    public TeamDeadException(String teamName) {
        super(teamName + "已经死了", 1);
    }

}
