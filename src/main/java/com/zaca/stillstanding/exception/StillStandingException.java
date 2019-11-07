package com.zaca.stillstanding.exception;

import com.zaca.stillstanding.domain.IApiResult;

/**
 * @author hundun
 * Created on 2019/10/12
 */
public class StillStandingException extends Exception implements IApiResult{
    
    private final int code;
    
    public StillStandingException(int code) {
        this.code = code;
    }
    
    public StillStandingException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    @Override
    public int getRetcode() {
        return code;
    }

    @Override
    public int getStatus() {
        return 400;
    }

    @Override
    public String getPayload() {
        return null;
    };

}
