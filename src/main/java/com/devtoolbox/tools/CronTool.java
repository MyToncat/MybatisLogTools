package com.devtoolbox.tools;

import com.devtoolbox.core.Algorithms;
import com.devtoolbox.core.ToolPanel;
import com.devtoolbox.core.ToolShell;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import java.awt.Color;

/**
 * Crontab 表达式解析/描述工具：将标准 5 段 cron 表达式转换为人类可读描述。
 */
public class CronTool extends ToolPanel {

    private JTextArea output;
    private JTextArea expr;

    public CronTool() {
        super("cron", "Crontab 解析", "开发工具", "解析标准 5 段 cron 表达式（分 时 日 月 周）为人类可读描述。");
    }

    @Override
    protected void build(ToolShell shell) {
        JPanel inputRow = new JPanel(new MigLayout("inset 0, fillx", "[right]8[grow]", "[]"));
        inputRow.add(new JLabel("表达式"), "right");
        expr = new JTextArea("0 9 * * 1-5");
        expr.setLineWrap(true);
        expr.setRows(2);
        inputRow.add(new JScrollPane(expr), "grow");
        shell.addContent(inputRow, "growx");

        output = new JTextArea();
        output.setLineWrap(true);
        output.setEditable(false);
        output.setBackground(new Color(0xF0, 0xF3, 0xF8));
        output.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        shell.addContent(new JLabel("解析结果"), "growx, gaptop 10");
        shell.addContent(new JScrollPane(output), "grow, hmin 120");

        JButton parseBtn = new JButton("解析");
        JButton clrBtn = new JButton("清空");
        ToolShell.styleButton(clrBtn, false);
        parseBtn.addActionListener(e -> parse(shell, expr.getText()));
        clrBtn.addActionListener(e -> {
            expr.setText("");
            output.setText("");
            shell.setStatus("已清空", ToolShell.StatusType.INFO);
        });
        shell.addHeaderButton(parseBtn);
        shell.addHeaderButton(clrBtn);

        parse(shell, expr.getText());
    }

    @Override
    public void seedDemo() {
        expr.setText("0 9 * * 1-5");
        try {
            output.setText(Algorithms.cronDescribe(expr.getText()));
        } catch (Exception ignore) {
        }
    }

    private void parse(ToolShell shell, String text) {
        if (text.trim().isEmpty()) {
            shell.setStatus("请输入 cron 表达式", ToolShell.StatusType.INFO);
            return;
        }
        try {
            output.setText(Algorithms.cronDescribe(text));
            shell.setStatus("解析成功", ToolShell.StatusType.SUCCESS);
        } catch (Exception ex) {
            output.setText("");
            shell.setStatus("解析失败：" + ex.getMessage(), ToolShell.StatusType.ERROR);
        }
    }
}
