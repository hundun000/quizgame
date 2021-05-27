package com.zaca.stillstanding.core.skill;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zaca.stillstanding.core.question.Question;
import com.zaca.stillstanding.core.skill.effect.AddBuffSkillEffect;
import com.zaca.stillstanding.core.skill.effect.ISkillEffect;
import com.zaca.stillstanding.exception.ModFormatException;

public class BaseSkillFactory {
    
    private static Map<String, SkillConstData> skills = new HashMap<>();
    static {
        registerSkill("跳过", "结束本题。本题不计入得分、答对数、答错数。",  Arrays.asList(Question.SKIP_ANSWER_TEXT));
        registerSkill("5050", "揭示2个错误选项。", Arrays.asList("2"));
        registerSkill("求助", "答题时间增加30秒，并且本题期间可与毒奶团交流。", Arrays.asList("30"));
        registerSkill("加时", "答题时间增加15秒。", Arrays.asList("15"));
        registerSkill("连击之力", "为自己增加一层“连击中”。", null, new AddBuffSkillEffect("连击中", 1));
    }
    private static void registerSkill(String name, String description, List<String> frontEndDataString, ISkillEffect... backendEffects) {
        SkillConstData skill;
        try {
            skill = new SkillConstData(name, description, frontEndDataString, backendEffects);
            skills.put(skill.getName(), skill);
        } catch (ModFormatException e) {
            e.printStackTrace();
        }
    }

    public static SkillConstData getSkill(String name) {
        return skills.get(name);
    }
    
    public static boolean exsistSkill(String skillName) {
        return skills.containsKey(skillName);
    }

}
