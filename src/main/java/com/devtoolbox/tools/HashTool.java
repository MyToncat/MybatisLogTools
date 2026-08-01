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
 * 哈希计算工具：MD5、SHA-1、SHA-256、SHA-512。
 */
public class HashTool extends ToolPanel {

    private JTextArea input;
    private JTextArea output;
    private JComboBox<String> algoBox;
    private JComboBox<String> caseBox;

    public HashTool() {
        super("hash", "MD5/SHA 哈希", "开发工具", "计算文本的 MD5 / SHA-1 / SHA-256 / SHA-512 摘要，支持大小写输出。");
    }

    @Override
    protected void build(ToolShell shell) {
        input = new JTextArea();
        input.setLineWrap(true);
        output = new JTextArea();
        output.setLineWrap(true);
        output.setEditable(false);

        shell.addContent(new JLabel("输入文本"), "growx");
        shell.addContent(new JScrollPane(input), "grow, hmin 100");

        JPanel opts = new JPanel(new MigLayout("inset 0", "[]8[]push[]8[]", "[]"));
        algoBox = new JComboBox<>(new String[]{"MD5", "SHA-1", "SHA-256", "SHA-512"});
        caseBox = new JComboBox<>(new String[]{"小写", "大写"});
        opts.add(new JLabel("算法"), "right");
        opts.add(algoBox);
        opts.add(new JLabel("输出"), "right");
        opts.add(caseBox);
        shell.addContent(opts, "growx, gaptop 8");

        shell.addContent(new JLabel("哈希结果"), "growx, gaptop 8");
        shell.addContent(new JScrollPane(output), "grow, hmin 100");

        JButton calcBtn = new JButton("计算");
        JButton clrBtn = new JButton("清空");
        ToolShell.styleButton(clrBtn, false);
        calcBtn.addActionListener(e -> calc(shell));
        clrBtn.addActionListener(e -> {
            input.setText("");
            output.setText("");
            shell.setStatus("已清空", ToolShell.StatusType.INFO);
        });
        shell.addHeaderButton(calcBtn);
        shell.addHeaderButton(clrBtn);
    }

    @Override
    public void seedDemo() {
        input.setText("DevToolbox 开发者工具箱");
        try {
            String algo = (String) algoBox.getSelectedItem();
            boolean upper = "大写".equals(caseBox.getSelectedItem());
            output.setText(Algorithms.hash(algo, input.getText(), upper));
        } catch (Exception ignore) {
        }
    }

    private void calc(ToolShell shell) {
        if (input.getText().isEmpty()) {
            shell.setStatus("请输入文本", ToolShell.StatusType.INFO);
            return;
        }
        try {
            String algo = (String) algoBox.getSelectedItem();
            boolean upper = "大写".equals(caseBox.getSelectedItem());
            output.setText(Algorithms.hash(algo, input.getText(), upper));
            shell.setStatus("已计算 " + algo, ToolShell.StatusType.SUCCESS);
        } catch (Exception ex) {
            output.setText("");
            shell.setStatus("计算失败：" + ex.getMessage(), ToolShell.StatusType.ERROR);
        }
    }
}
