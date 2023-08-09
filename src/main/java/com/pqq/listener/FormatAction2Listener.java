package com.pqq.listener;

import com.pqq.utils.BasicFormatterUtils;
import com.pqq.utils.JTextPaneFormatUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *   格式化SQl
 */
public class FormatAction2Listener implements ActionListener {

    JTextPane jTextPane;

    public FormatAction2Listener(JTextPane jTextPane) {
        this.jTextPane = jTextPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = jTextPane.getText();
        if (StringUtils.isEmpty(text) ) {
            return;
        }
        BasicFormatterUtils formatterUtils = new BasicFormatterUtils();
        // 格式化sql
        text = formatterUtils.format(text).replaceAll("\r","");
        jTextPane.setText(text);
        // 高亮显示
        JTextPaneFormatUtils.highLightFormat(jTextPane);
    }
}
