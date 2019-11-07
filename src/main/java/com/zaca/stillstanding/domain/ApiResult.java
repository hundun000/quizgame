package com.zaca.stillstanding.domain;

import com.alibaba.fastjson.JSON;
import com.zaca.stillstanding.exception.StillStandingException;

/**
 * @author hundun
 * Created on 2019/11/04
 */
public class ApiResult implements IApiResult {
    
    private static final String SUCCESS_MESSAGE = "SUCCESS";
    private static final int SUCCESS_STATUS = 200;
    
    private final String message;
    private final int status;
    private final String payload;
    private final int retcode;
    
    public ApiResult() {
        this((String) null);
    }
    
    public ApiResult(String payload) {
        this.message = SUCCESS_MESSAGE;
        this.status = SUCCESS_STATUS;
        this.payload = payload;
        this.retcode = 0;
    }
    
    public ApiResult(Object payload) {
        this(JSON.toJSONString(payload));
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    @Override
    public int getStatus() {
        return status;
    }
    
    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public int getRetcode() {
        return retcode;
    }

    
}
