package com.devtoolbox.tools;

import com.devtoolbox.core.Algorithms;
import com.devtoolbox.core.ToolPanel;
import com.devtoolbox.core.ToolShell;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.time.ZoneId;

/**
 * 时间戳与日期互转工具：支持秒/毫秒级时间戳，本地时区与 UTC。
 */
public class TimestampTool extends ToolPanel {

    private JTextField tsField;
    private JTextField dateField;
    private JComboBox<String> unitBox;
    private JComboBox<String> zoneBox;

    public TimestampTool() {
        super("timestamp", "时间戳转换", "时间工具", "时间戳（秒 / 毫秒）与日期时间互转，支持本地时区与 UTC。");
    }

    @Override
    protected void build(ToolShell shell) {
        unitBox = new JComboBox<>(new String[]{"秒 (s)", "毫秒 (ms)"});
        zoneBox = new JComboBox<>(new String[]{"本地时区", "UTC"});

        tsField = new JTextField();
        dateField = new JTextField();

        JPanel cfg = new JPanel(new MigLayout("inset 0", "[]8[]push[]8[]", "[]"));
        cfg.add(new JLabel("单位"), "right");
        cfg.add(unitBox);
        cfg.add(new JLabel("时区"), "right");
        cfg.add(zoneBox);
        shell.addContent(cfg, "growx");

        shell.addContent(new JLabel("时间戳"), "growx, gaptop 10");
        shell.addContent(tsField, "growx");
        shell.addContent(new JLabel("日期时间 (yyyy-MM-dd HH:mm:ss)"), "growx, gaptop 8");
        shell.addContent(dateField, "growx");

        JButton tsToDate = new JButton("时间戳 → 日期");
        JButton dateToTs = new JButton("日期 → 时间戳");
        JButton nowBtn = new JButton("填入当前");
        JButton clrBtn = new JButton("清空");
        ToolShell.styleButton(nowBtn, false);
        ToolShell.styleButton(clrBtn, false);
        tsToDate.addActionListener(e -> toDate(shell));
        dateToTs.addActionListener(e -> toTs(shell));
        nowBtn.addActionListener(e -> {
            long now = System.currentTimeMillis();
            tsField.setText(String.valueOf(unitBox.getSelectedItem().equals("秒 (s)") ? now / 1000 : now));
            shell.setStatus("已填入当前时间戳", ToolShell.StatusType.INFO);
        });
        clrBtn.addActionListener(e -> {
            tsField.setText("");
            dateField.setText("");
            shell.setStatus("已清空", ToolShell.StatusType.INFO);
        });
        shell.addHeaderButton(tsToDate);
        shell.addHeaderButton(dateToTs);
        shell.addHeaderButton(nowBtn);
        shell.addHeaderButton(clrBtn);
    }

    @Override
    public void seedDemo() {
        tsField.setText("1700000000");
        try {
            String ts = tsField.getText().trim();
            String unit = (String) unitBox.getSelectedItem();
            long ms = "毫秒".equals(unit) ? Long.parseLong(ts) : Long.parseLong(ts) * 1000L;
            java.time.ZoneId z = "UTC".equals(zoneBox.getSelectedItem()) ? java.time.ZoneId.of("UTC") : java.time.ZoneId.systemDefault();
            java.time.ZonedDateTime dt = java.time.Instant.ofEpochMilli(ms).atZone(z);
            dateField.setText(dt.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } catch (Exception ignore) {
        }
    }

    private ZoneId zone() {
        return zoneBox.getSelectedItem().equals("UTC") ? ZoneId.of("UTC") : ZoneId.systemDefault();
    }

    private void toDate(ToolShell shell) {
        if (tsField.getText().trim().isEmpty()) {
            shell.setStatus("请先输入时间戳", ToolShell.StatusType.INFO);
            return;
        }
        try {
            long ts = Long.parseLong(tsField.getText().trim());
            boolean isSeconds = unitBox.getSelectedItem().equals("秒 (s)");
            dateField.setText(Algorithms.tsToDate(ts, isSeconds, zone()));
            shell.setStatus("转换成功", ToolShell.StatusType.SUCCESS);
        } catch (Exception ex) {
            dateField.setText("");
            shell.setStatus("无效时间戳", ToolShell.StatusType.ERROR);
        }
    }

    private void toTs(ToolShell shell) {
        if (dateField.getText().trim().isEmpty()) {
            shell.setStatus("请先输入日期时间", ToolShell.StatusType.INFO);
            return;
        }
        try {
            boolean isSeconds = unitBox.getSelectedItem().equals("秒 (s)");
            long ts = Algorithms.dateToTs(dateField.getText(), isSeconds, zone());
            tsField.setText(String.valueOf(ts));
            shell.setStatus("转换成功", ToolShell.StatusType.SUCCESS);
        } catch (Exception ex) {
            tsField.setText("");
            shell.setStatus("无效日期，格式应为 yyyy-MM-dd HH:mm:ss", ToolShell.StatusType.ERROR);
        }
    }
}
