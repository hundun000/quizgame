package com.zaca.stillstanding.domain.skill;

import java.util.HashMap;
import java.util.Map;

public class BaseSkillFactory {
    
    private static Map<String, BaseSkill> skills = new HashMap<>();
    static {
        registerSkill(new BaseSkill("跳过", "结束本题。本题不计入得分、答对数、答错数。"));
        registerSkill(new BaseSkill("5050", "揭示x个错误选项。", "{\"x\":2}"));
        registerSkill(new BaseSkill("求助", "答题时间增加x秒，并且本题期间可与毒奶团交流。", "{\"x\":30}"));
        registerSkill(new BaseSkill("加时", "答题时间增加x秒。", "{\"x\":15}"));
    }
    private static void registerSkill(BaseSkill skill) {
        skills.put(skill.getName(), skill);
    }

    public static BaseSkill getSkill(String name) {
        return skills.get(name);
    }

}
