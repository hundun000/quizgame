package com.zaca.stillstanding.exception;
/**
 * @author hundun
 * Created on 2020/05/20
 */
public class ModFormatException extends StillStandingException {

    /**
     * 
     */
    private static final long serialVersionUID = 4915826309430319294L;

    public ModFormatException(String modContent, String targetType) {
        super("Mod中的 " + modContent + " 不是正确的" + targetType + "格式。", -1);
    }

}
