package com.zaca.stillstanding.domain.team;
/**
 * @author hundun
 * Created on 2020/01/11
 */
public enum HealthType {
    
    /**
     * 剩余可连续大错数
     */
    CONSECUTIVE_WRONG_AT_LEAST(0),
    ;
    
    
    private final int code;
    
    public int getCode() {
        return code;
    }
    
    private HealthType(int code) {
        this.code = code;
    }

}
