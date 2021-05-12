package com.zaca.stillstanding.exception;


/**
 * @author hundun
 * Created on 2019/10/12
 */
public class StillStandingException extends Exception {
    
    private static final long serialVersionUID = 6572553799656923229L;
    
    private final int code;
    
    public StillStandingException(int code) {
        this.code = code;
    }
    
    public StillStandingException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    
    public int getRetcode() {
        return code;
    }

    
    public int getStatus() {
        return 400;
    }

    
    public String getPayload() {
        return null;
    };
    
    
    public String getMessage() {
        return super.getMessage();
    }

}
