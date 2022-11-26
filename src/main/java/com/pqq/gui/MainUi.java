package com.pqq.gui;

import com.pqq.listener.FormatAction2Listener;
import com.pqq.listener.FormatActionListener;
import com.pqq.listener.FormatJsonBtnListener;
import com.pqq.listener.JTextFieldHintListener;
import com.pqq.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUi {
    private JPanel root;
    private JTabbedPane tabbedPane1;
    private JTextField sqlTextField;
    private JTextField paramField;
    private JButton formatBtn;
    private JTextPane resultTextPane;
    private JButton formatBtn2;
    private JScrollPane originPanel;
    private JScrollPane dealtPanel;
    private JTextPane textPane1;
    private JTextPane textPane2;
    private JButton formatJsonBtn;


    public MainUi() {
        // 预初始化
        initConfig();
    }


    /**
     * 初始化
     */
    private void initConfig() {
        //
        initJFrame();
        // 初始化 sqlTextField
        initTextField(sqlTextField,Constants.hintTextSQl);
        // 初始化参数
        initTextField(paramField, Constants.hintTextParam);
        // 初始化按钮
        initBtn();
    }

    /**
     * jframe 初始化
     */
    private void initJFrame()  {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        int drive_width = defaultToolkit.getScreenSize().width;
        int drive_height = defaultToolkit.getScreenSize().height;
        int width = Constants.WIDTH;
        int height = Constants.HEIGHT;

        JFrame frame = new JFrame(Constants.UI_TITLE);
        frame.setContentPane(this.root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocation((drive_width - width)/2 ,(drive_height - height)/2);
        frame.setVisible(true);
    }


    /**
     * 按钮初始化
     */
    private void initBtn() {
        formatBtn.addActionListener(new FormatActionListener(sqlTextField,paramField,resultTextPane));
        formatBtn2.addActionListener(new FormatAction2Listener(resultTextPane));
        formatJsonBtn.addActionListener(new FormatJsonBtnListener(textPane1,textPane2));
    }

    /**
     * 输入框预编译.
     * @param jTextField  jTextField
     * @param hitText hitText
     */
    private void initTextField(JTextField jTextField,String hitText) {
        jTextField.setText(hitText);
        jTextField.setForeground(Color.GRAY);
        jTextField.addFocusListener(new JTextFieldHintListener(jTextField,hitText));
    }

    public static void main(String[] args) {
        MainUi mainUi = new MainUi();
    }
}
