package hundun.quizgame.core.tool;

import java.util.List;
import hundun.quizgame.core.model.domain.SkillSlotRuntimeModel;
import hundun.quizgame.core.prototype.RolePrototype;
import hundun.quizgame.core.prototype.TeamPrototype;
import hundun.quizgame.core.prototype.match.MatchStrategyType;
import hundun.quizgame.core.prototype.skill.SkillSlotPrototype;
import hundun.quizgame.core.view.buff.BuffRuntimeView;
import hundun.quizgame.core.view.skill.SkillSlotRuntimeView;
import hundun.quizgame.core.view.team.TeamRuntimeView;

/**
 * @author hundun
 * Created on 2021/07/17
 */
public class TextHelper {
    
    public static MatchStrategyType chineseToMatchStrategyType(String matchMode) {
        switch (matchMode) {
            case "无尽模式":
                return MatchStrategyType.ENDLESS;
            case "单人模式":
                return MatchStrategyType.PRE;
            case "双人模式":
                return MatchStrategyType.MAIN;
            default:
                return null;
        }
    }
    
    public static String teamsNormalText(List<TeamRuntimeView> dtos) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("队伍状态:\n");
        for (TeamRuntimeView dto : dtos) {
            stringBuilder.append(dto.getName()).append(" ");
            stringBuilder.append("得分:").append(dto.getMatchScore()).append(" ");
            stringBuilder.append("生命:").append(dto.getHealth()).append(" ");
            if (dto.getRuntimeBuffs().size() > 0) {
                stringBuilder.append("Buff:\n");
                for (BuffRuntimeView buffDTO : dto.getRuntimeBuffs()) {
                    stringBuilder.append(buffDTO.getName()).append("x").append(buffDTO.getDuration()).append(" ").append(buffDTO.getDescription()).append("\n");
                }
            }
            if (dto.getRoleRuntimeInfo() != null) {
                stringBuilder.append("英雄:").append(dto.getRoleRuntimeInfo().getName()).append(" 技能:\n");
                for (SkillSlotRuntimeView skillSlotRuntimeView : dto.getRoleRuntimeInfo().getSkillSlotRuntimeViews()) {
                    stringBuilder.append(skillSlotRuntimeView.getName()).append(":").append(skillSlotRuntimeView.getRemainUseTime()).append(" ");
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
    
    public static String teamsDetailText(List<TeamRuntimeView> teamRuntimeDTOs, List<TeamPrototype> teamPrototypes) {
        StringBuilder stringBuilder = new StringBuilder();
        
        
        stringBuilder.append("队伍详情:\n");
        for (int i = 0; i < teamRuntimeDTOs.size(); i++) {
            TeamRuntimeView teamRuntimeView = teamRuntimeDTOs.get(i);
            TeamPrototype teamPrototypeSimpleView = teamPrototypes.get(i);
            
            
            stringBuilder.append(teamPrototypeSimpleView.getName()).append(" 生命:").append(teamRuntimeView.getHealth()).append("\n");
            if (teamPrototypeSimpleView.getPickTags().size() > 0) {
                stringBuilder.append("Pick:");
                teamPrototypeSimpleView.getPickTags().forEach(tag -> stringBuilder.append(tag).append("、"));
                stringBuilder.setLength(stringBuilder.length() - 1);
                stringBuilder.append("\n");
            }
            if (teamPrototypeSimpleView.getBanTags().size() > 0) {
                stringBuilder.append("Ban:");
                teamPrototypeSimpleView.getBanTags().forEach(tag -> stringBuilder.append(tag).append("、"));
                stringBuilder.setLength(stringBuilder.length() - 1);
                stringBuilder.append("\n");
            }
            
            RolePrototype rolePrototype = teamPrototypeSimpleView.getRolePrototype();
            if (rolePrototype != null) {
                stringBuilder.append("英雄:").append(rolePrototype.getName()).append(" 介绍:").append(rolePrototype.getDescription()).append("\n");
                for (int j = 0; j < rolePrototype.getSkillSlotPrototypes().size(); j++) {
                    SkillSlotPrototype skillSlotPrototype = rolePrototype.getSkillSlotPrototypes().get(j);
                    stringBuilder.append("技能").append(j + 1).append(":").append(skillSlotPrototype.getName()).append(" ");
                    stringBuilder.append("次数:").append(skillSlotPrototype.getFullUseTime()).append(" ");
                    stringBuilder.append("效果:").append(skillSlotPrototype.getDescription()).append("\n");
                }
                stringBuilder.append("\n");
            }
            
        }
        return stringBuilder.toString();
    }
    
    public static int answerTextToInt(String text) {
        switch (text) {
            case "A":
            case "a":
                return 0;
            case "B":
            case "b":
                return 1;
            case "C":
            case "c":
                return 2;
            case "D":
            case "d":
                return 3;
            default:
                return -1;
        }
        
    }
    
    public static String intToAnswerText(int value) {
        switch (value) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "D";
            default:
                return "?";
        }
        
    }
    
    
    
    
    

}
