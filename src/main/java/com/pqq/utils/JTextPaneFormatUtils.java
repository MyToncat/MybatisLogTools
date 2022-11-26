package com.pqq.utils;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *   JTextPaneFormatUtils 格式化 工具
 */
public final class JTextPaneFormatUtils {

    private JTextPaneFormatUtils() {}

    public final static void highLightFormat(JTextPane jTextPane) {
        // 按规则添加颜色
        String reg = "UPDATE|WHERE|SET|AND|null|\\d+";
        Pattern compile = Pattern.compile(reg);
        Matcher matcher = compile.matcher(jTextPane.getText().trim().replace("\r",""));
        while (matcher.find()) {
            jTextPane.getStyledDocument().setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), initAttributeConfig(), false);
        }
    }


    /**
     * 初始化加粗,红色
     */
    public final static SimpleAttributeSet initAttributeConfig() {
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(simpleAttributeSet, Color.BLUE);
        StyleConstants.setBold(simpleAttributeSet, true);
        return simpleAttributeSet;
    }
    /**
     * 画笔
     * @param jTextPane
     */
    private void highLight( JTextPane jTextPane) {
        DefaultHighlighter.DefaultHighlightPainter defaultHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
        jTextPane.setText(jTextPane.getText());
        Highlighter highlighter = jTextPane.getHighlighter();
        try {
            highlighter.addHighlight(0, jTextPane.getDocument().getLength(), defaultHighlightPainter);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

}
