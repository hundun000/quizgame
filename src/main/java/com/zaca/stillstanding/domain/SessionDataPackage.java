package com.zaca.stillstanding.domain;

import java.util.List;
import java.util.Set;

import com.zaca.stillstanding.domain.match.BaseMatch;
import com.zaca.stillstanding.domain.question.Question;

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
