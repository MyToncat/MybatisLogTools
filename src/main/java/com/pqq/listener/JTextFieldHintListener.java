package com.pqq.listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextFieldHintListener implements FocusListener {
    private String hintText;
    private JTextField textField;

    public JTextFieldHintListener(JTextField textField) {
        this.textField = textField;
    }

    public JTextFieldHintListener(JTextField textField,String hintText) {
        this.hintText = hintText;
        this.textField = textField;
    }

    //获取焦点时，清空提示内容
    @Override
    public void focusGained(FocusEvent e) {
        //获取焦点时，清空提示内容
        String temp = textField.getText();
        if(temp.equals(hintText)) {
            textField.setText("");
            textField.setForeground(Color.BLACK);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        //失去焦点时，没有输入内容，显示提示内容
        String temp = textField.getText();
        if(temp.equals("")) {
            textField.setForeground(Color.GRAY);
            textField.setText(hintText);
        }
    }
}
