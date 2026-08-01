package com.devtoolbox.tools;

import com.devtoolbox.core.Algorithms;
import com.devtoolbox.core.ToolPanel;
import com.devtoolbox.core.ToolShell;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import java.util.List;

/**
 * UUID 生成工具：支持批量生成、去除连字符、大小写。
 */
public class UuidTool extends ToolPanel {

    private JTextArea output;
    private JSpinner countSpinner;
    private JCheckBox noDashCb;
    private JCheckBox upperCb;

    public UuidTool() {
        super("uuid", "UUID 生成", "开发工具", "生成 UUID v4，支持批量、去连字符与大小写输出。");
    }

    @Override
    protected void build(ToolShell shell) {
        JPanel opts = new JPanel(new MigLayout("inset 0", "[]8[]push[]8[]8[]", "[]"));
        countSpinner = new JSpinner(new javax.swing.SpinnerNumberModel(5, 1, 1000, 1));
        noDashCb = new JCheckBox("去除连字符");
        upperCb = new JCheckBox("大写");
        opts.add(new JLabel("数量"), "right");
        opts.add(countSpinner);
        opts.add(noDashCb);
        opts.add(upperCb);
        shell.addContent(opts, "growx");

        output = new JTextArea();
        output.setLineWrap(true);
        shell.addContent(new JLabel("生成结果"), "growx, gaptop 8");
        shell.addContent(new JScrollPane(output), "grow, hmin 160");

        JButton genBtn = new JButton("生成");
        JButton copyBtn = new JButton("复制全部");
        JButton clrBtn = new JButton("清空");
        ToolShell.styleButton(copyBtn, false);
        ToolShell.styleButton(clrBtn, false);
        genBtn.addActionListener(e -> generate(shell, noDashCb.isSelected(), upperCb.isSelected()));
        copyBtn.addActionListener(e -> {
            if (output.getText().isEmpty()) return;
            output.selectAll();
            output.copy();
            output.setSelectionStart(0);
            output.setSelectionEnd(0);
            shell.setStatus("已复制全部 UUID", ToolShell.StatusType.SUCCESS);
        });
        clrBtn.addActionListener(e -> {
            output.setText("");
            shell.setStatus("已清空", ToolShell.StatusType.INFO);
        });
        shell.addHeaderButton(genBtn);
        shell.addHeaderButton(copyBtn);
        shell.addHeaderButton(clrBtn);
    }

    @Override
    public void seedDemo() {
        try {
            int n = (Integer) countSpinner.getValue();
            boolean noDash = noDashCb.isSelected();
            boolean upper = upperCb.isSelected();
            java.util.List<String> list = Algorithms.uuid(n, noDash, upper);
            output.setText(String.join("\n", list));
        } catch (Exception ignore) {
        }
    }

    private void generate(ToolShell shell, boolean noDash, boolean upper) {
        int n = (Integer) countSpinner.getValue();
        List<String> list = Algorithms.uuid(n, noDash, upper);
        output.setText(String.join("\n", list));
        shell.setStatus("已生成 " + n + " 个 UUID", ToolShell.StatusType.SUCCESS);
    }
}
