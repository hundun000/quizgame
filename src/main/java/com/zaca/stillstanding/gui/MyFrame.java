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
@Component
public class MyFrame extends JFrame implements CommandLineRunner{

    @Autowired
    QuestionService questionService;
    
    @Autowired
    TeamService teamService;
    
    @Autowired
    PreMatch match;
    
    private JPanel contentPane;
    private JTextField input;
    private JTextArea output;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MyFrame frame = new MyFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     * @throws Exception 
     */
    public MyFrame() {
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
        setVisible(true);
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

    @Override
    public void run(String... args) throws Exception {
        initGame();
        initUIBind();
        initEventListener();
    }
}
