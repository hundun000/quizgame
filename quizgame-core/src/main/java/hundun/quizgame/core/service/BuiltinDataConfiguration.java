package hundun.quizgame.core.service;

import java.util.ArrayList;
import java.util.Arrays;

import hundun.quizgame.core.context.IQuizCoreComponent;
import hundun.quizgame.core.context.QuizCoreContext;
import hundun.quizgame.core.exception.QuizgameException;
import hundun.quizgame.core.model.domain.buff.CombBuffStrategy;
import hundun.quizgame.core.prototype.RolePrototype;
import hundun.quizgame.core.prototype.buff.BuffPrototype;
import hundun.quizgame.core.prototype.buff.BuffStrategyType;
import hundun.quizgame.core.prototype.skill.AddBuffSkillEffect;
import hundun.quizgame.core.prototype.skill.SkillSlotPrototype;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/07/17
 */
@Slf4j
public class BuiltinDataConfiguration implements IQuizCoreComponent {
    public static String DEMO_LIST_TEAM_NAME_0 = "游客";
    public static String DEMO_LIST_TEAM_NAME_1 = "游客1";
    public static String DEMO_LIST_TEAM_NAME_2 = "游客2";
    
    private TeamService teamService;
    private RoleSkillService roleSkillService;
    private BuffService buffService;
    
    @Override
    public void wire(QuizCoreContext quizCoreContext) {
        this.teamService = quizCoreContext.teamService;
        this.roleSkillService = quizCoreContext.roleSkillService;
        this.buffService = quizCoreContext.buffService;
    }
    
    private void initTestData() throws QuizgameException {

        teamService.registerTeam("砍口垒同好组", Arrays.asList("单机游戏"), Arrays.asList("动画"), "ZACA娘");
        teamService.registerTeam("方舟同好组", Arrays.asList("动画"), Arrays.asList("单机游戏"), "ZACA娘");
    }
    
    @Override
    public void postWired() {
        
        try {
            initTestData();
            
            teamService.registerTeam(BuiltinDataConfiguration.DEMO_LIST_TEAM_NAME_0, new ArrayList<>(0), new ArrayList<>(0), null);
            teamService.registerTeam(BuiltinDataConfiguration.DEMO_LIST_TEAM_NAME_1, new ArrayList<>(0), new ArrayList<>(0), null);
            teamService.registerTeam(BuiltinDataConfiguration.DEMO_LIST_TEAM_NAME_2, new ArrayList<>(0), new ArrayList<>(0), null);
            
            

            roleSkillService.registerSkill(new SkillSlotPrototype("跳过", "结束本题。本题不计入得分、答对数、答错数。", null, null, 2));
            roleSkillService.registerSkill(new SkillSlotPrototype("5050", "揭示2个错误选项。", Arrays.asList("2"), null, 2));
            roleSkillService.registerSkill(new SkillSlotPrototype("求助", "答题时间增加30秒，并且本题期间可与毒奶团交流。", Arrays.asList("30"), null, 2));
            roleSkillService.registerSkill(new SkillSlotPrototype("连击之力", "为自己增加一层“连击中”。", null, Arrays.asList(new AddBuffSkillEffect("连击中", 1)), 2));
            
            
            roleSkillService.registerRole(new RolePrototype(
                    "ZACA娘", 
                    "主人公。", 
                    Arrays.asList(
                            roleSkillService.getSkillSlotPrototype("5050"),
                            roleSkillService.getSkillSlotPrototype("求助"),
                            roleSkillService.getSkillSlotPrototype("跳过"),
                            roleSkillService.getSkillSlotPrototype("连击之力")
                    )
            ));
            

            buffService.registerBuffPrototype(new BuffPrototype(
                    "连击中", 
                    "答题正确时，额外获得与“连击中”层数相同的分数，且“连击中”层数加1（最大为3层）；否则，失去所有“连击中”层数。", 
                    3, 
                    BuffStrategyType.SCORE_MODIFY
                    ));
        } catch (Exception e) {
            log.error("initBuiltinData error:", e);
        }
        
    }
}
