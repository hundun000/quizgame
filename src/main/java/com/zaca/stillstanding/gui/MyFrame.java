package com.zaca.stillstanding.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zaca.stillstanding.domain.event.EventType;
import com.zaca.stillstanding.domain.event.MatchEvent;
import com.zaca.stillstanding.domain.match.BaseMatch;
import com.zaca.stillstanding.domain.match.PreMatch;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.service.GameService;
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

    
    
    
    PreMatch match;
    
    private JPanel contentPane;
    private JTextField input;
    private JTextArea dataOutput;
    private JTextArea viewOutput; 
    JLabel lblTime;
    
    int timerCount;
    Timer secondTimer;
    boolean ignoreSecondTimer;
    
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
    public MyFrame(PreMatch match) {
        this.match = match;
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 832, 723);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        dataOutput = new JTextArea();
        dataOutput.setBounds(22, 55, 403, 551);
        contentPane.add(dataOutput);
        
        input = new JTextField();
        input.setBounds(22, 623, 403, 29);
        contentPane.add(input);
        input.setColumns(10);
        
        viewOutput = new JTextArea();
        viewOutput.setBounds(452, 25, 347, 627);
        contentPane.add(viewOutput);
        
        lblTime = new JLabel("Time:");
        lblTime.setBounds(22, 23, 108, 29);
        contentPane.add(lblTime);
    }
    
    private void initGame() throws Exception {

        MatchEvent event = match.start();
        renewTimerCount(event.getData().getIntValue("time"));
    }
    
    private void renewTimerCount(int value) {
        ignoreSecondTimer = false;
        timerCount = value;
        lblTime.setText(String.valueOf(timerCount));
    }
    
    private void renewTimerCountByAdd(int add) {
        renewTimerCount(timerCount + add);
    }
    
    private void initUIData() {
        dataOutput.setText(JSON.toJSONString(FormatTool.matchToShortJSON(match), SerializerFeature.PrettyFormat) + "\n");
    }
    
    private void initEvent() {
        secondTimer = new Timer();
        secondTimer.schedule(new SecondTimerTask(this), 1000, 1000);
        
        input.addKeyListener(new MyKeyAdapter());
    }
    
    class MyKeyAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_ENTER){
               String command = input.getText();
               input.setText("");
               
               if (!handleGUICommand(command)) {
                   try {
                    match.commandLineControl(command);
                    handleEvent(match);
                } catch (StillStandingException e1) {
                    dataOutput.setText("StillStandingException:" + e1.getMessage());
                    disableTimer();
                }
                   
                   
               }

            }
          }
    }
    
    private boolean handleGUICommand(String command) {
        switch (command) {
        case "the world":
            ignoreSecondTimer = !ignoreSecondTimer;
            break;
        default:
            return false;
        }
        return true;
    }

    @Override
    public void whenReceive() {
        if (!ignoreSecondTimer) {
            lblTime.setForeground(Color.BLACK);
            renewTimerCountByAdd(-1);
            
            if (timerCount == 0) {
                lblTime.setForeground(Color.RED);
                ignoreSecondTimer = true;
                
                try {
                    match.teamAnswerTimeout();
                    handleEvent(match);
                } catch (StillStandingException e) {
                    e.printStackTrace();
                }
                
            }
            
        } else {
            lblTime.setForeground(Color.BLUE);
        }
    }
    
    private void disableTimer() {
        ignoreSecondTimer = true;
        lblTime.setForeground(Color.BLUE);
        lblTime.setText("unused");;
    }
    
    private void handleEvent(BaseMatch match) {
        if (match.containsEventByType(EventType.FINISH)) {
            dataOutput.setText("FINISH!");
            disableTimer();
            return;
        }
        
        if (match.containsEventByType(EventType.SWITCH_QUESTION)) {
            renewTimerCount(match.getEventByType(EventType.SWITCH_QUESTION).getData().getIntValue("time"));
            dataOutput.setText(JSON.toJSONString(FormatTool.matchToShortJSON(match), SerializerFeature.PrettyFormat) + "\n");
            return;
        }
        
    }
}
