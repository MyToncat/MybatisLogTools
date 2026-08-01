package com.devtoolbox.tools;

import com.devtoolbox.core.Algorithms;
import com.devtoolbox.core.ToolPanel;
import com.devtoolbox.core.ToolShell;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * URL 编码 / 解码工具。
 */
public class UrlTool extends ToolPanel {

    private JTextArea input;
    private JTextArea output;

    public UrlTool() {
        super("url", "URL 编解码", "开发工具", "对 URL 参数进行百分号编码 / 解码（UTF-8）。");
    }

    @Override
    protected void build(ToolShell shell) {
        input = new JTextArea();
        input.setLineWrap(true);
        output = new JTextArea();
        output.setLineWrap(true);
        output.setEditable(false);

        shell.addContent(new JLabel("输入"), "growx");
        shell.addContent(new JScrollPane(input), "grow, hmin 100");
        shell.addContent(new JLabel("结果"), "growx, gaptop 8");
        shell.addContent(new JScrollPane(output), "grow, hmin 100");

        JButton encBtn = new JButton("编码");
        JButton decBtn = new JButton("解码");
        JButton clrBtn = new JButton("清空");
        ToolShell.styleButton(clrBtn, false);
        encBtn.addActionListener(e -> encode(shell));
        decBtn.addActionListener(e -> decode(shell));
        clrBtn.addActionListener(e -> {
            input.setText("");
            output.setText("");
            shell.setStatus("已清空", ToolShell.StatusType.INFO);
        });
        shell.addHeaderButton(encBtn);
        shell.addHeaderButton(decBtn);
        shell.addHeaderButton(clrBtn);
    }

    @Override
    public void seedDemo() {
        input.setText("开发者工具箱 https://example.com/搜索?q=中文");
        try {
            output.setText(Algorithms.urlEncode(input.getText()));
        } catch (Exception ignore) {
        }
    }

    private void encode(ToolShell shell) {
        if (input.getText().isEmpty()) {
            shell.setStatus("请输入内容", ToolShell.StatusType.INFO);
            return;
        }
        output.setText(Algorithms.urlEncode(input.getText()));
        shell.setStatus("已编码", ToolShell.StatusType.SUCCESS);
    }

    private void decode(ToolShell shell) {
        if (input.getText().isEmpty()) {
            shell.setStatus("请输入内容", ToolShell.StatusType.INFO);
            return;
        }
        try {
            output.setText(Algorithms.urlDecode(input.getText()));
            shell.setStatus("已解码", ToolShell.StatusType.SUCCESS);
        } catch (Exception ex) {
            output.setText("");
            shell.setStatus("解码失败：" + ex.getMessage(), ToolShell.StatusType.ERROR);
        }
    }
}
