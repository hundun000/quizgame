package com.zaca.stillstanding.domain.match;
/**
 * @author hundun
 * Created on 2019/09/06
 */

import java.util.LinkedList;
import java.util.List;

import com.zaca.stillstanding.domain.question.AnswerType;

public class AnswerRecorder {
    
    class RecordNode{
        private String teamName;
        private String answer;
        private String questionId;
        private AnswerType answerType;
        
        public RecordNode(String teamName, String answer, String questionId, AnswerType answerType) {
            this.teamName = teamName;
            this.answer = answer;
            this.questionId = questionId;
            this.answerType = answerType;
        }
        
        public boolean isMe(String teamName) {
            return this.teamName.equals(teamName);
        }
        
        public AnswerType getAnswerType() {
            return answerType;
        }
    }
    
    
    /**
     * 绑定LinkedList实现，以使用LinkedList特有方法
     */
    LinkedList<RecordNode> nodes = new LinkedList<>();
    
    public void addRecord(String teamName, String answer, String questionId, AnswerType answerType) {
        // 仿栈
        nodes.addFirst(new RecordNode(teamName, answer, questionId, answerType));
    }
    
    
    
    /**
     * 返回teamName连续错误次数是否大于等于num。
     * @param teamName
     * @param num
     * @return
     */
    public boolean isConsecutiveWrongAtLeastByTeam(String teamName, int num) {
        return count(teamName, AnswerType.WRONG, num, true) >= num;
    }
    
    public boolean isSumAtLeastByTeam(String teamName, int num) {
        return count(teamName, null, num, false) >= num;
    }
    

    /**
     * 可用统计某队[连续/累计][正确/错误/全部]回答的个数
     * @param teamName
     * @param check 计数的类型；null:计数任意回答
     * @param max 计数等于max时停止
     * @param consecutive 是否要求连续满足计数的类型；
     * @return
     */
    public int count(String teamName, AnswerType check, int max, boolean consecutive) {
        int num = 0;
        for (RecordNode node : nodes) {
            if(!node.isMe(teamName)) {
                continue;
            }
            
            if (node.getAnswerType() == AnswerType.SKIPPED) {
                continue;
            } else {
                if(check == null || node.getAnswerType() == check) {
                    // 类型符合
                    num++;
                } else {
                    if (consecutive) {
                       // 类型不符合，且要求连续符合，故跳出
                       break; 
                    }
                }
            }
            
            if (num == max) {
                break;
            }
        }
        return num;
    }

}
