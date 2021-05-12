package com.zaca.stillstanding.gui;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.zaca.stillstanding.core.match.BaseMatch;
import com.zaca.stillstanding.dto.match.MatchSituationDTO;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JLabel;

/**
 * @author hundun
 * Created on 2019/09/11
 */
public class MyFrame extends JFrame implements ISecondEventReceiver{

    
    
    
    MatchSituationDTO match;
    
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
    public MyFrame(MatchSituationDTO match) {
        this.match = match;
        
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

        //match.start();
        //renewTimerCount(match.getEvents().get(0).getPayload().value("time").asInt());
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
        //dataOutput.setText(JSON.toJSONString(FormatTool.matchToShortJSON(match), SerializerFeature.PrettyFormat) + "\n");
    }
    
    private void initEvent() {
        secondTimer = new Timer();
        secondTimer.schedule(new SecondTimerTask(this), 1000, 1000);
        
        input.addKeyListener(new MyKeyAdapter());
    }
    
    class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_ENTER){
               String command = input.getText();
               input.setText("");
               
//               if (!handleGUICommand(command)) {
//                   try {
//                    match.commandLineControl(command);
//                    handleEvent(match);
//                } catch (StillStandingException e1) {
//                    dataOutput.setText("StillStandingException:" + e1.getMessage());
//                    disableTimer();
//                }
//                   
//                   
//               }

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
                
//                try {
//                    match.teamAnswerTimeout();
//                    handleEvent(match);
//                } catch (StillStandingException e) {
//                    e.printStackTrace();
//                }
                
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
//        if (match.containsEventByType(EventType.FINISH)) {
//            dataOutput.setText("FINISH!");
//            disableTimer();
//            return;
//        }
//        
//        if (match.containsEventByType(EventType.SWITCH_QUESTION)) {
//            //renewTimerCount(match.getEventByType(EventType.SWITCH_QUESTION).getPayload().value("time").asInt());
//            dataOutput.setText(JSON.toJSONString(FormatTool.matchToShortJSON(match), SerializerFeature.PrettyFormat) + "\n");
//            return;
//        }
        
    }
}
