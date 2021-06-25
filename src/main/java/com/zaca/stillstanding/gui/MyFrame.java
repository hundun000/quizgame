package com.zaca.stillstanding.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.zaca.stillstanding.core.match.BaseMatch;
import com.zaca.stillstanding.core.question.Question;
import com.zaca.stillstanding.dto.match.MatchConfigDTO;
import com.zaca.stillstanding.dto.match.MatchSituationDTO;
import com.zaca.stillstanding.exception.StillStandingException;
import com.zaca.stillstanding.service.GameService;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JLabel;

/**
 * @author hundun
 * Created on 2019/09/11
 */
public class MyFrame extends JFrame implements ISecondEventReceiver{

    GameService gameService;
    
    String matchId;
    MatchSituationDTO matchSituationDTO;
    
    private JPanel contentPane;
    private JTextField input;
    private JTextArea matchSituationOutput;
    private JTextArea viewOutput; 
    private JButton createAndStartButton;
    private JButton nextQuestionButton;
    private JButton answerAButton;
    private JButton answerBButton;
    private JButton answerCButton;
    private JButton answerDButton;
    JLabel lblTime;
    
    int timerCount;
    Timer secondTimer;
    //boolean ignoreSecondTimer;

    /**
     * Create the frame.
     * @param match2 
     * @param teamService2 
     * @param questionService2 
     * @throws Exception 
     */
    public MyFrame(GameService gameService) {
        this.gameService = gameService;
        
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 832, 923);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        matchSituationOutput = new JTextArea();
        matchSituationOutput.setBounds(22, 55, 403, 551);
        matchSituationOutput.setLineWrap(true);
        contentPane.add(matchSituationOutput);
        
        input = new JTextField();
        input.setBounds(22, 623, 403, 29);
        contentPane.add(input);
        input.setColumns(10);
        
        createAndStartButton = new JButton("createAndStart");
        createAndStartButton.setBounds(10, 660, 50, 29);
        createAndStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createAndStartMatch();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPane.add(createAndStartButton);
        
        nextQuestionButton = new JButton("nextQuestion");
        nextQuestionButton.setBounds(70, 660, 50, 29);
        nextQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    matchSituationDTO = gameService.nextQustion(matchId);
                } catch (StillStandingException e1) {
                    e1.printStackTrace();
                }
                updateByNewMatchSituation();
            }
        });
        contentPane.add(nextQuestionButton);
        
        answerAButton = new JButton("A");
        answerAButton.setBounds(130, 660, 30, 29);
        answerAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        contentPane.add(nextQuestionButton);
        
        viewOutput = new JTextArea();
        viewOutput.setBounds(452, 25, 347, 627);
        contentPane.add(viewOutput);
        
        lblTime = new JLabel("Time:");
        lblTime.setBounds(22, 23, 108, 29);
        contentPane.add(lblTime);
        
        secondTimer = new Timer();
        secondTimer.schedule(new SecondTimerTask(this), 1000, 1000);
        
        input.addKeyListener(new MyKeyAdapter());
    }
    
    private void createAndStartMatch() throws Exception {
        MatchConfigDTO matchConfigDTO = new MatchConfigDTO();
        matchConfigDTO.setTeamNames(Arrays.asList("游客"));
        matchConfigDTO.setQuestionPackageName("questions");
        
        matchSituationDTO = gameService.createEndlessMatch(matchConfigDTO);
        this.matchId = matchSituationDTO.getId();
        
        matchSituationDTO = gameService.startMatch(matchId);
        
        updateByNewMatchSituation();
    }
    
    private void updateByNewMatchSituation() {
        
        matchSituationOutput.setText(matchSituationDTO.toString());
        
        if (matchSituationDTO.getSwitchQuestionEvent() != null) {
            int newTimeCount = matchSituationDTO.getSwitchQuestionEvent().getTime();
            
            timerCount = newTimeCount;
            lblTime.setText(String.valueOf(timerCount));
        }
        
        if (matchSituationDTO.getAnswerResultEvent() != null) {
            timerCount = 0;
            lblTime.setText(String.valueOf(timerCount));
        }

    }
    

    
    class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_ENTER){
               String command = input.getText();
               input.setText("");
               

               try {
                   matchSituationDTO = gameService.commandLineControl(matchId, command);
                   updateByNewMatchSituation();
               } catch (StillStandingException e1) {
                   e1.printStackTrace();
               }



            }
          }
    }
    


    @Override
    public void whenReceive() {

            if (timerCount > 0) {
                lblTime.setForeground(Color.BLACK);
                timerCount -= 1;
                lblTime.setText(String.valueOf(timerCount));
                
                if (timerCount == 0) {
                    lblTime.setForeground(Color.RED);
                    try {
                        matchSituationDTO = gameService.teamAnswer(matchId, Question.TIMEOUT_ANSWER_TEXT);
                    } catch (StillStandingException e) {
                        e.printStackTrace();
                    }
                    updateByNewMatchSituation();
                }
            } else {
                lblTime.setForeground(Color.BLUE);
            }
    }
    

    

}
