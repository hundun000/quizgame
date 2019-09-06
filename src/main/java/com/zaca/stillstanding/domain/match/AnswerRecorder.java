package com.zaca.stillstanding.domain.match;
/**
 * @author hundun
 * Created on 2019/09/06
 */

import java.util.LinkedList;
import java.util.List;

public class AnswerRecorder {
    
    class RecordNode{
        private String teamName;
        private String answer;
        private String questionId;
        private boolean correct;
        
        public RecordNode(String teamName, String answer, String questionId, boolean correct) {
            this.teamName = teamName;
            this.answer = answer;
            this.questionId = questionId;
            this.correct = correct;
        }
        
        public boolean isMe(String teamName) {
            return this.teamName.equals(teamName);
        }
        
        public boolean isCorrect() {
            return correct;
        }
    }
    
    
    /**
     * 绑定LinkedList实现，以使用LinkedList特有方法
     */
    LinkedList<RecordNode> nodes = new LinkedList<>();
    
    public void addRecord(String teamName, String answer, String questionId, boolean correct) {
        // 仿栈
        nodes.addFirst(new RecordNode(teamName, answer, questionId, correct));
    }
    
    
    
    /**
     * 返回teamName连续错误次数是否大于等于num。
     * @param teamName
     * @param num
     * @return
     */
    public boolean isConsecutiveWrongAtLeastByTeam(String teamName, int num) {
        return count(teamName, Boolean.FALSE, num, true) > num;
    }
    
    public boolean isSumAtLeastByTeam(String teamName, int num) {
        return count(teamName, null, num, false) > num;
    }
    

    /**
     * 可用统计某队[连续/累计][正确/错误/全部]回答的个数
     * @param teamName
     * @param check TRUE:仅计数正确回答；FALSE:仅计数错误回答；null:仅计数任意回答
     * @param max 计数等于max时停止
     * @param consecutive true:统计全部记录；false:当check不满足时停止；
     * @return
     */
    public int count(String teamName, Boolean check, int max, boolean consecutive) {
        int num = 0;
        for (RecordNode node : nodes) {
            if(!node.isMe(teamName)) {
                continue;
            }
            if(check == null || node.isCorrect() == check) {
                num++;
            } else {
                if (consecutive) {
                   break; 
                }
            }
            if (num == max) {
                break;
            }
        }
        return num;
    }

}
