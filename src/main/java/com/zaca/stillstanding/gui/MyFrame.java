package com.zaca.stillstanding.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zaca.stillstanding.domain.match.PreMatch;
import com.zaca.stillstanding.service.QuestionService;
import com.zaca.stillstanding.service.TeamService;
import com.zaca.stillstanding.tool.FormatTool;
import com.zaca.stillstanding.tool.QuestionTool;

import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author hundun
 * Created on 2019/09/11
 */
public class MyFrame extends JFrame {

    QuestionService questionService;
    
    TeamService teamService;
    
    PreMatch match;
    
    private JPanel contentPane;
    private JTextField input;
    private JTextArea output;

    
    
    public void start() throws Exception {
        setVisible(true);
        initGame();
        initUIBind();
        initEventListener();
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
        setBounds(100, 100, 702, 723);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        output = new JTextArea();
        output.setBounds(10, 10, 664, 625);
        contentPane.add(output);
        
        input = new JTextField();
        input.setBounds(10, 645, 664, 29);
        contentPane.add(input);
        input.setColumns(10);

    }
    
    private void initGame() throws Exception {
        questionService.initQuestions(QuestionTool.TEST_SMALL_PACKAGE_NAME);
        teamService.initForTest("砍口垒同好组");
        match.init("砍口垒同好组");
        match.start();
    }
    
    private void initUIBind() {
        output.setText(JSON.toJSONString(FormatTool.coupleMatchTOJSON(match), SerializerFeature.PrettyFormat) + "\n");
    }
    
    private void initEventListener() {
        input.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                  if(e.getKeyCode()==KeyEvent.VK_ENTER){
                     String command = input.getText();
                     input.setText("");
                     match.commandLineControl(command);
                     output.setText(JSON.toJSONString(FormatTool.coupleMatchTOJSON(match), SerializerFeature.PrettyFormat) + "\n");
                  }               
                }
              });
    }

    
}
