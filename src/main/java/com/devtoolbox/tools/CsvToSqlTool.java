package com.devtoolbox.tools;

import com.devtoolbox.core.Algorithms;
import com.devtoolbox.core.ToolPanel;
import com.devtoolbox.core.ToolShell;
import com.devtoolbox.core.Ui;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * CSV 转 SQL 工具：将 CSV 文本转换为 INSERT 语句，支持表头、分隔符、引号策略与合并写入。
 */
public class CsvToSqlTool extends ToolPanel {

    private JTextArea input;
    private JTextArea output;
    private JTextField tableField;
    private JTextField delimField;
    private JCheckBox headerCb;
    private JCheckBox quoteAllCb;
    private JCheckBox mergeCb;

    public CsvToSqlTool() {
        super("csv2sql", "CSV 转 SQL", "开发工具", "将 CSV 数据转换为 INSERT 语句，可配置表名、分隔符、表头与引号策略。");
    }

    @Override
    protected void build(ToolShell shell) {
        // ---- 参数行 ----
        tableField = new JTextField("users");
        delimField = new JTextField(",");
        delimField.setColumns(3);
        headerCb = new JCheckBox("首行为列名", true);
        quoteAllCb = new JCheckBox("所有值加引号", false);
        mergeCb = new JCheckBox("合并为单条 INSERT", true);

        JPanel cfg = new JPanel(new MigLayout("inset 0, wrap 5, gapx 10, gapy 6", "[right]8[grow]16[right]8[]16[right]8[]16[right]8[]", "[]"));
        cfg.add(new JLabel("表名"), "right");
        cfg.add(tableField, "grow");
        cfg.add(new JLabel("分隔符"), "right");
        cfg.add(delimField);
        cfg.add(headerCb);
        cfg.add(quoteAllCb);
        cfg.add(mergeCb);
        shell.addContent(cfg, "growx");

        // ---- 双栏：CSV 输入 / SQL 输出 ----
        input = new JTextArea();
        input.setLineWrap(false);
        input.setFont(Ui.MONO_FONT);
        output = new JTextArea();
        output.setLineWrap(false);
        output.setEditable(false);
        output.setFont(Ui.MONO_FONT);
        output.setBackground(new java.awt.Color(0xF0, 0xF3, 0xF8));

        JPanel dual = new JPanel(new MigLayout("inset 0, fillx, wrap 2, gap 12", "[grow][grow]", "[]0[]"));
        dual.add(new JLabel("CSV 输入"), "growx");
        dual.add(new JLabel("SQL 输出"), "growx");
        dual.add(new JScrollPane(input), "grow, hmin 200");
        dual.add(new JScrollPane(output), "grow, hmin 200");
        shell.addContent(dual, "grow, gaptop 12");

        JButton convBtn = new JButton("转换");
        JButton copyBtn = new JButton("复制 SQL");
        JButton clrBtn = new JButton("清空");
        ToolShell.styleButton(copyBtn, false);
        ToolShell.styleButton(clrBtn, false);
        convBtn.addActionListener(e -> convert(shell));
        copyBtn.addActionListener(e -> {
            if (output.getText().isEmpty()) return;
            output.selectAll();
            output.copy();
            output.setSelectionStart(0);
            output.setSelectionEnd(0);
            shell.setStatus("已复制 SQL", ToolShell.StatusType.SUCCESS);
        });
        clrBtn.addActionListener(e -> {
            input.setText("");
            output.setText("");
            shell.setStatus("已清空", ToolShell.StatusType.INFO);
        });
        shell.addHeaderButton(convBtn);
        shell.addHeaderButton(copyBtn);
        shell.addHeaderButton(clrBtn);

        // 示例
        input.setText("id,name,age,email\n1,张三,28,zhangsan@example.com\n2,李四,31,lisi@example.com");
        convert(shell);
    }

    @Override
    public void seedDemo() {
        try {
            convert(null);
        } catch (Exception ignore) {
        }
    }

    private void convert(ToolShell shell) {
        if (input.getText().trim().isEmpty()) {
            shell.setStatus("请输入 CSV 数据", ToolShell.StatusType.INFO);
            return;
        }
        if (tableField.getText().trim().isEmpty()) {
            shell.setStatus("请填写表名", ToolShell.StatusType.INFO);
            return;
        }
        try {
            char delim = delimField.getText().isEmpty() ? ',' : delimField.getText().charAt(0);
            String sql = Algorithms.csvToSql(input.getText(), tableField.getText().trim(), delim,
                    headerCb.isSelected(), quoteAllCb.isSelected(), !mergeCb.isSelected());
            output.setText(sql);
            shell.setStatus("转换成功", ToolShell.StatusType.SUCCESS);
        } catch (Exception ex) {
            output.setText("");
            shell.setStatus("转换失败：" + ex.getMessage(), ToolShell.StatusType.ERROR);
        }
    }
}
