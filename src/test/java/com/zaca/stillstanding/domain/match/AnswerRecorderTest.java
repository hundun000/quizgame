package com.zaca.stillstanding.domain.match;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zaca.stillstanding.core.match.AnswerRecorder;
import com.zaca.stillstanding.domain.dto.AnswerType;

/**
 * @author hundun
 * Created on 2019/09/07
 */
public class AnswerRecorderTest {

    static AnswerRecorder recorderSingle = new AnswerRecorder();
    
    
    @BeforeClass
    public static void beforeClass() {
        recorderSingle.addRecord("t1", "", "", AnswerType.CORRECT);
        recorderSingle.addRecord("t1", "", "", AnswerType.WRONG);
        recorderSingle.addRecord("t1", "", "", AnswerType.CORRECT);
        recorderSingle.addRecord("t1", "", "", AnswerType.WRONG);
        recorderSingle.addRecord("t1", "", "", AnswerType.WRONG);
    }
    
    @Test
    public void isSumAtLeastByTeam() {
        assertEquals(true, recorderSingle.isSumAtLeast("t1", 4));
        assertEquals(true, recorderSingle.isSumAtLeast("t1", 5));
        assertEquals(false, recorderSingle.isSumAtLeast("t1", 6));
    }
    
    @Test
    public void isConsecutiveWrongAtLeastByTeam() {
        assertEquals(true, recorderSingle.isConsecutiveWrongAtLeast("t1", 1));
        assertEquals(true, recorderSingle.isConsecutiveWrongAtLeast("t1", 2));
        assertEquals(false, recorderSingle.isConsecutiveWrongAtLeast("t1", 3));
    }

}
