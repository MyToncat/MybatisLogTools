package com.pqq.listener;

import com.pqq.utils.BasicFormatterUtils;
import com.pqq.utils.Constants;
import com.pqq.utils.JTextPaneFormatUtils;
import com.pqq.utils.MyBatisLogParseUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *   SQL 和 参数组装
 */
public class FormatActionListener implements ActionListener {
    JTextField sqlTextField;
    JTextField paramTextField;
    JTextPane jTextPane;

    public FormatActionListener(JTextField sqlTextField, JTextField paramTextField, JTextPane jTextPane) {
        this.sqlTextField = sqlTextField;
        this.paramTextField = paramTextField;
        this.jTextPane = jTextPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        sqlTextField.setText("UPDATE mp_user SET name=? WHERE id=? AND name=? AND age=? AND email=? AND deleted=0");
        paramTextField.setText("null, 1(Long), 张三(String), 18(Integer), x@y.com(String)");

        // 解析sql
        String dealedSql = MyBatisLogParseUtils.parseSql(sqlTextField.getText(),
                MyBatisLogParseUtils.parseParams(paramTextField.getText())).toString();
        BasicFormatterUtils formatterUtils = new BasicFormatterUtils();
        // 格式化sql
        dealedSql = formatterUtils.format(dealedSql);

        // 清空此前结果
        jTextPane.setText("");
        if (!(StringUtils.equals(sqlTextField.getText(), Constants.hintTextSQl) ||
                StringUtils.equals(paramTextField.getText(), Constants.hintTextParam))) {
            jTextPane.setText(dealedSql.trim());
        }
        // 高亮显示
        JTextPaneFormatUtils.highLightFormat(jTextPane);
    }

}
