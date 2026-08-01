package com.devtoolbox.tools;

import com.devtoolbox.core.Algorithms;
import com.devtoolbox.core.ToolPanel;
import com.devtoolbox.core.ToolShell;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * 颜色转换 + 拾取工具：HEX ↔ RGB 互转，并通过 JColorChooser 拾取颜色。
 */
public class ColorTool extends ToolPanel {

    private JTextField hexField;
    private JTextField rField;
    private JTextField gField;
    private JTextField bField;
    private JLabel preview;

    public ColorTool() {
        super("color", "颜色转换 / 拾取", "颜色工具", "HEX 与 RGB 互转，可点击拾取颜色并实时预览。");
    }

    @Override
    protected void build(ToolShell shell) {
        preview = new JLabel();
        preview.setOpaque(true);
        preview.setBackground(Color.WHITE);
        preview.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        preview.setPreferredSize(new java.awt.Dimension(0, 56));

        hexField = new JTextField("#FFFFFF");
        rField = new JTextField("255");
        gField = new JTextField("255");
        bField = new JTextField("255");

        shell.addContent(new JLabel("预览"), "growx");
        shell.addContent(preview, "growx, hmin 56");

        JPanel grid = new JPanel(new MigLayout("inset 0, wrap 2, gap 10", "[right]16[grow]", "[]8[]8[]"));
        grid.add(new JLabel("HEX"));
        grid.add(hexField, "growx");
        grid.add(new JLabel("R"));
        grid.add(rField, "growx");
        grid.add(new JLabel("G"));
        grid.add(gField, "growx");
        grid.add(new JLabel("B"));
        grid.add(bField, "growx");
        shell.addContent(grid, "growx, gaptop 10");

        JButton hexToRgb = new JButton("HEX → RGB");
        JButton rgbToHex = new JButton("RGB → HEX");
        JButton pickBtn = new JButton("拾取颜色");
        JButton clrBtn = new JButton("清空");
        ToolShell.styleButton(clrBtn, false);
        hexToRgb.addActionListener(e -> hexToRgb(shell));
        rgbToHex.addActionListener(e -> rgbToHex(shell));
        pickBtn.addActionListener(e -> pick(shell));
        clrBtn.addActionListener(e -> {
            hexField.setText("");
            rField.setText("");
            gField.setText("");
            bField.setText("");
            preview.setBackground(Color.WHITE);
            shell.setStatus("已清空", ToolShell.StatusType.INFO);
        });
        shell.addHeaderButton(hexToRgb);
        shell.addHeaderButton(rgbToHex);
        shell.addHeaderButton(pickBtn);
        shell.addHeaderButton(clrBtn);

        hexField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                hexToRgb(shell);
            }
        });
    }

    @Override
    public void seedDemo() {
        hexField.setText("#4A90D9");
        try {
            int[] rgb = Algorithms.hexToRgb(hexField.getText());
            rField.setText(String.valueOf(rgb[0]));
            gField.setText(String.valueOf(rgb[1]));
            bField.setText(String.valueOf(rgb[2]));
            java.awt.Color c = new java.awt.Color(rgb[0], rgb[1], rgb[2]);
            preview.setBackground(c);
        } catch (Exception ignore) {
        }
    }

    private void hexToRgb(ToolShell shell) {
        try {
            int[] rgb = Algorithms.hexToRgb(hexField.getText());
            rField.setText(String.valueOf(rgb[0]));
            gField.setText(String.valueOf(rgb[1]));
            bField.setText(String.valueOf(rgb[2]));
            preview.setBackground(new Color(rgb[0], rgb[1], rgb[2]));
            shell.setStatus("已转换", ToolShell.StatusType.SUCCESS);
        } catch (Exception ex) {
            preview.setBackground(Color.WHITE);
            shell.setStatus("无效 HEX 颜色", ToolShell.StatusType.ERROR);
        }
    }

    private void rgbToHex(ToolShell shell) {
        try {
            int r = Integer.parseInt(rField.getText().trim());
            int g = Integer.parseInt(gField.getText().trim());
            int b = Integer.parseInt(bField.getText().trim());
            hexField.setText(Algorithms.rgbToHex(r, g, b));
            preview.setBackground(new Color(clamp(r), clamp(g), clamp(b)));
            shell.setStatus("已转换", ToolShell.StatusType.SUCCESS);
        } catch (Exception ex) {
            shell.setStatus("RGB 需为 0-255 整数", ToolShell.StatusType.ERROR);
        }
    }

    private void pick(ToolShell shell) {
        Color c = JColorChooser.showDialog(null, "选择颜色", preview.getBackground());
        if (c != null) {
            rField.setText(String.valueOf(c.getRed()));
            gField.setText(String.valueOf(c.getGreen()));
            bField.setText(String.valueOf(c.getBlue()));
            hexField.setText(String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue()));
            preview.setBackground(c);
            shell.setStatus("已拾取颜色", ToolShell.StatusType.SUCCESS);
        }
    }

    private int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }
}
