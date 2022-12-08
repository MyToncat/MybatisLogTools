package com.pqq.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pqq.utils.JTextPaneFormatUtils.initAttributeConfig;

public class FormatJsonBtnListener implements ActionListener {
    JTextPane textPane1;
    JTextPane textPane2;
    ObjectMapper objectMapper;

    public FormatJsonBtnListener(JTextPane textPane1, JTextPane textPane2) {
        this.textPane1 = textPane1;
        this.textPane2 = textPane2;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String originJson = textPane1.getText();
        if (StringUtils.isEmpty(originJson)) {
            return;
        }
        try {


            Object obj = objectMapper.readValue(originJson, Object.class);
            // \r影响正则匹配
            String dealtJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj).replaceAll("\r","");
            textPane2.setText(dealtJson);

            // 高亮Json
            String  pattern =  "\"([a-zA-Z_]+)\" :";
            Pattern compile = Pattern.compile(pattern);
            Matcher matcher = compile.matcher(dealtJson);
            StyledDocument styledDocument = textPane2.getStyledDocument();
            while (matcher.find()) {
                styledDocument.setCharacterAttributes(matcher.start(1), matcher.end(1) - matcher.start(1), attributeSetForKey(), false);
            }


            String  pattern1 =  ": \\d+";
            Pattern compile1 = Pattern.compile(pattern1);
            Matcher matcher1 = compile1.matcher(dealtJson);
            while (matcher1.find()) {
                styledDocument.setCharacterAttributes(matcher1.start(0), matcher1.end(0) - matcher1.start(0), attributeSetForNumber(), false);
            }
        }
        catch (JsonProcessingException ex) {
            JOptionPane.showMessageDialog(null, ex.getOriginalMessage(), "错误",JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 初始化加粗,红色
     */
    public SimpleAttributeSet attributeSetForKey() {
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(simpleAttributeSet, Color.magenta);
        return simpleAttributeSet;
    }

    /**
     * 初始化加粗,红色
     */
    public SimpleAttributeSet attributeSetForNumber() {
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(simpleAttributeSet, new Color(0,255,0));
        return simpleAttributeSet;
    }
}
