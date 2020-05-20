package com.zaca.stillstanding.domain.skill;

import java.util.HashMap;
import java.util.Map;

import com.zaca.stillstanding.domain.question.Question;
import com.zaca.stillstanding.domain.skill.effect.AddBuffSkillEffect;
import com.zaca.stillstanding.domain.skill.effect.ISkillEffect;
import com.zaca.stillstanding.exception.ModFormatException;

public class BaseSkillFactory {
    
    private static Map<String, BaseSkill> skills = new HashMap<>();
    static {
        registerSkill("跳过", "结束本题。本题不计入得分、答对数、答错数。", "{\"auto_answer\":\"" + Question.SKIP_ANSWER_TEXT + "\"}");
        registerSkill("5050", "揭示2个错误选项。", "{\"show_option_num\":2}");
        registerSkill("求助", "答题时间增加30秒，并且本题期间可与毒奶团交流。", "{\"add_time\":30}");
        registerSkill("加时", "答题时间增加15秒。", "{\"add_time\":15}");
        registerSkill("连击之力", "为自己增加一层“连击中”。", null, new AddBuffSkillEffect("连击中", 1));
    }
    private static void registerSkill(String name, String description, String frontEndDataString, ISkillEffect... backendEffects) {
        BaseSkill skill;
        try {
            skill = new BaseSkill(name, description, frontEndDataString, backendEffects);
            skills.put(skill.getName(), skill);
        } catch (ModFormatException e) {
            e.printStackTrace();
        }
    }

    public static BaseSkill getSkill(String name) {
        return skills.get(name);
    }

}
