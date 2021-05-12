package com.zaca.stillstanding.core;

import java.util.List;
import java.util.Set;

import com.zaca.stillstanding.core.match.BaseMatch;
import com.zaca.stillstanding.core.question.Question;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class SessionDataPackage {
    String sessionId;
    List<String> questionIds;
    List<Question> dirtyQuestionIds;
    Set<String> tags;
    boolean allowImageResource = true;
    boolean allowVoiceResource = false;
    BaseMatch match;
}
