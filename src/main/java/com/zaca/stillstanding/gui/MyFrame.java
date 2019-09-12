package com.zaca.stillstanding.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zaca.stillstanding.domain.match.MatchEvent;
import com.zaca.stillstanding.domain.match.PreMatch;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.TeamService;
import com.zaca.stillstanding.tool.FormatTool;
import com.zaca.stillstanding.tool.QuestionTool;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * @author hundun
 * Created on 2019/09/11
 */
public class MyFrame extends JFrame implements ISecondEventReceiver{

    QuestionService questionService;
    
    TeamService teamService;
    
    PreMatch match;
    
    private JPanel contentPane;
    private JTextField input;
    private JTextArea dataOutput;
    private JTextArea viewOutput; 
    JLabel lblTime;
    
    int timerCount;
    Timer secondTimer;
    
    public void start() throws Exception {
        setVisible(true);
        initGame();
        initUIData();
        initEvent();
    }

    /**
     * Create the frame.
     * @param match2 
     * @param teamService2 
     * @param questionService2 
     * @throws Exception 
     */
    public MyFrame(QuestionService questionService, TeamService teamService, PreMatch match) {
        this.questionService = questionService;
        this.teamService = teamService;
        this.match = match;
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 963, 723);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        dataOutput = new JTextArea();
        dataOutput.setBounds(379, 23, 542, 629);
        contentPane.add(dataOutput);
        
        input = new JTextField();
        input.setBounds(22, 623, 329, 29);
        contentPane.add(input);
        input.setColumns(10);
        
        viewOutput = new JTextArea();
        viewOutput.setBounds(22, 62, 329, 551);
        contentPane.add(viewOutput);
        
        lblTime = new JLabel("Time:");
        lblTime.setBounds(22, 23, 108, 29);
        contentPane.add(lblTime);
    }
    
    private void initGame() throws Exception {
        questionService.initQuestions(QuestionTool.TEST_SMALL_PACKAGE_NAME);
        teamService.initForTest("砍口垒同好组");
        match.init("砍口垒同好组");
        
        MatchEvent event = match.start();
        renewTimerCount(event.getData().getIntValue("time"));
    }
    
    private void renewTimerCount(int value) {
        timerCount = value;
        lblTime.setText(String.valueOf(timerCount));
    }
    
    private void renewTimerCountByAdd(int add) {
        renewTimerCount(timerCount + add);
    }
    
    private void initUIData() {
        dataOutput.setText(JSON.toJSONString(FormatTool.coupleMatchTOJSON(match), SerializerFeature.PrettyFormat) + "\n");
    }
    
    private void initEvent() {
        secondTimer = new Timer();
        secondTimer.schedule(new SecondTimerTask(this), 1000, 1000);
        
        input.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                  if(e.getKeyCode()==KeyEvent.VK_ENTER){
                     String command = input.getText();
                     input.setText("");
                     match.commandLineControl(command);
                     dataOutput.setText(JSON.toJSONString(FormatTool.coupleMatchTOJSON(match), SerializerFeature.PrettyFormat) + "\n");
                  }               
                }
              });
    }

    @Override
    public void whenReceive() {
        renewTimerCountByAdd(-1);
    }
    
    
}
