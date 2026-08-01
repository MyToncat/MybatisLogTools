package com.devtoolbox.tools;

import com.devtoolbox.core.Algorithms;
import com.devtoolbox.core.ToolPanel;
import com.devtoolbox.core.ToolShell;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Base64 编码 / 解码工具。支持 URL 安全模式与字符集选择。
 */
public class Base64Tool extends ToolPanel {

    private JTextArea input;
    private JTextArea output;
    private JComboBox<String> charsetBox;
    private JComboBox<String> modeBox;

    public Base64Tool() {
        super("base64", "Base64 工具", "开发工具", "文本与 Base64 互转，支持标准 / URL 安全模式及字符集选择。");
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

        JPanel opts = new JPanel(new MigLayout("inset 0", "[]8[]push[]8[]", "[]"));
        charsetBox = new JComboBox<>(new String[]{"UTF-8", "GBK", "ISO-8859-1"});
        modeBox = new JComboBox<>(new String[]{"标准 Base64", "URL 安全 Base64"});
        opts.add(new JLabel("字符集"), "right");
        opts.add(charsetBox);
        opts.add(new JLabel("模式"), "right");
        opts.add(modeBox);
        shell.addContent(opts, "growx, gaptop 8");

        shell.addContent(new JLabel("输出"), "growx, gaptop 8");
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
        input.setText("你好，开发者工具箱 DevToolbox！");
        try {
            String charset = (String) charsetBox.getSelectedItem();
            boolean urlSafe = "URL 安全".equals(modeBox.getSelectedItem());
            output.setText(Algorithms.base64Encode(input.getText(), charset, urlSafe));
        } catch (Exception ignore) {
        }
    }

    private void encode(ToolShell shell) {
        if (input.getText().isEmpty()) {
            shell.setStatus("请输入内容", ToolShell.StatusType.INFO);
            return;
        }
        boolean urlSafe = modeBox.getSelectedItem().equals("URL 安全 Base64");
        output.setText(Algorithms.base64Encode(input.getText(), (String) charsetBox.getSelectedItem(), urlSafe));
        shell.setStatus("已编码", ToolShell.StatusType.SUCCESS);
    }

    private void decode(ToolShell shell) {
        if (input.getText().isEmpty()) {
            shell.setStatus("请输入内容", ToolShell.StatusType.INFO);
            return;
        }
        boolean urlSafe = modeBox.getSelectedItem().equals("URL 安全 Base64");
        try {
            output.setText(Algorithms.base64Decode(input.getText(), (String) charsetBox.getSelectedItem(), urlSafe));
            shell.setStatus("已解码", ToolShell.StatusType.SUCCESS);
        } catch (Exception ex) {
            output.setText("");
            shell.setStatus("解码失败：输入不是合法的 Base64 串", ToolShell.StatusType.ERROR);
        }
    }
}
