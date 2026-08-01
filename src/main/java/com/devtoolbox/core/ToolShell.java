package com.devtoolbox.core;

import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

/**
 * 统一的工具外壳：顶部标题栏（标题 + 说明 + 主操作按钮），中间内容区，底部状态条。
 * 所有工具内容放在内容区，保证风格一致、布局合理。
 */
public final class ToolShell extends JPanel {

    private final JLabel titleLabel = new JLabel();
    private final JLabel descLabel = new JLabel();
    private final JPanel headerRight = new JPanel(new MigLayout("inset 0, align right", "[]8[]", "[]"));
    private final JPanel content = new JPanel(new MigLayout(Ui.CONTENT_CONSTRAINTS + ", gapy " + Ui.GAP, "[grow]", "[]"));
    private final JLabel statusLabel = new JLabel(" ");

    public ToolShell(String title, String description) {
        super(new BorderLayout(0, 0));
        buildHeader(title, description);
        buildStatus();

        content.setBackground(Ui.BG);
        add(wrapHeader(), BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        add(statusBar(), BorderLayout.SOUTH);
    }

    private JPanel headerBar;
    private void buildHeader(String title, String description) {
        titleLabel.setText(title);
        titleLabel.setFont(Ui.TITLE_FONT);
        titleLabel.setForeground(Ui.TEXT);
        descLabel.setText(description);
        descLabel.setFont(Ui.DESC_FONT);
        descLabel.setForeground(Ui.SUBTLE);

        headerBar = new JPanel(new MigLayout("inset " + Ui.PAD + " " + Ui.PAD + " " + (Ui.PAD - 4) + " " + Ui.PAD,
                "[grow][]", "[]4[]"));
        headerBar.setBackground(Ui.PANEL);
        headerBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Ui.BORDER));
        headerBar.add(titleLabel, "cell 0 0");
        headerBar.add(headerRight, "cell 1 0, aligny top");
        headerBar.add(descLabel, "cell 0 1 2 1");
    }

    private JPanel wrapHeader() {
        return headerBar;
    }

    private JPanel statusBar() {
        JPanel bar = new JPanel(new MigLayout("inset 6 " + Ui.PAD, "[grow]", "[]"));
        bar.setBackground(Ui.PANEL);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Ui.BORDER));
        statusLabel.setFont(Ui.STATUS_FONT);
        statusLabel.setForeground(Ui.SUBTLE);
        bar.add(statusLabel, "growx");
        return bar;
    }

    private void buildStatus() {
        // 占位，状态条在 statusBar() 中构建
    }

    /** 添加主操作按钮到标题栏右侧（可添加多个，建议只放主要操作）。 */
    public void addHeaderButton(JButton btn) {
        styleButton(btn, true);
        headerRight.add(btn);
    }

    /** 在内容区添加组件。 */
    public void addContent(Component c, String constraints) {
        content.add(c, constraints);
    }

    public JPanel getContentPanel() {
        return content;
    }

    /** 显示状态信息，type 控制颜色。 */
    public void setStatus(String msg, StatusType type) {
        statusLabel.setText(msg);
        Color color = Ui.SUBTLE;
        if (type == StatusType.SUCCESS) color = Ui.SUCCESS;
        else if (type == StatusType.ERROR) color = Ui.ERROR;
        statusLabel.setForeground(color);
    }

    public static void styleButton(JButton btn, boolean primary) {
        btn.setFont(new java.awt.Font("Microsoft YaHei", java.awt.Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        if (primary) {
            btn.setBackground(Ui.PRIMARY);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        } else {
            btn.setBackground(Ui.PANEL);
            btn.setForeground(Ui.TEXT);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Ui.BORDER),
                    BorderFactory.createEmptyBorder(6, 16, 6, 16)));
        }
    }

    public enum StatusType { INFO, SUCCESS, ERROR }
}
