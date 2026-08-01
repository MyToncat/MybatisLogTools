package com.devtoolbox.core;

import java.awt.Color;
import java.awt.Font;

/**
 * UI 设计规范：统一间距、字体、配色，保证整体简洁一致。
 */
public final class Ui {

    private Ui() {
    }

    public static final int PAD = 16;          // 内容区内边距
    public static final int GAP = 10;          // 控件间距

    // 主题色
    public static final Color PRIMARY = new Color(0x2D, 0x6C, 0xDF);   // 主操作蓝
    public static final Color PRIMARY_HOVER = new Color(0x1B, 0x57, 0xC4);
    public static final Color BG = new Color(0xF5, 0xF6, 0xF8);        // 工作区背景
    public static final Color PANEL = Color.WHITE;                    // 卡片背景
    public static final Color BORDER = new Color(0xE2, 0xE5, 0xEA);    // 边框灰
    public static final Color TEXT = new Color(0x33, 0x33, 0x33);      // 主文字
    public static final Color SUBTLE = new Color(0x88, 0x8C, 0x94);    // 次要文字
    public static final Color SUCCESS = new Color(0x2E, 0x9E, 0x5B);   // 成功绿
    public static final Color ERROR = new Color(0xD9, 0x3A, 0x3A);     // 错误红

    public static final Font TITLE_FONT = new Font("Microsoft YaHei", Font.BOLD, 18);
    public static final Font DESC_FONT = new Font("Microsoft YaHei", Font.PLAIN, 12);
    public static final Font STATUS_FONT = new Font("Microsoft YaHei", Font.PLAIN, 12);

    /**
     * 代码/SQL 文本区字体。Consolas 不含中文字形，直接指定会让中文
     * 显示为乱码，因此优先使用支持中文的微软雅黑（Windows 上可读、
     * 含 CJK 字形）；其它平台回退到逻辑等宽字体。
     */
    public static final Font MONO_FONT = new Font("Microsoft YaHei", Font.PLAIN, 13);

    /** MigLayout 通用内容约束：单列出内容，自动撑满。 */
    public static final String CONTENT_CONSTRAINTS = "fill, wrap 1, inset " + PAD;
}
