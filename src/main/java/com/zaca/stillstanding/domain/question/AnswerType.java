package com.zaca.stillstanding.domain.question;
/**
 * @author hundun
 * Created on 2019/10/08
 */
public enum AnswerType {
    CORRECT,
    WRONG,
    SKIPPED;
    
    public static AnswerType get(Boolean isCorrect) {
        if (isCorrect == null) {
            return SKIPPED;
        } else {
            return isCorrect ? CORRECT : WRONG;
        }
    }
}
