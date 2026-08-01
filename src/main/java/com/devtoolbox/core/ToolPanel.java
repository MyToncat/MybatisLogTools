package com.devtoolbox.core;

import javax.swing.JPanel;

/**
 * 所有工具的基类。统一使用 {@link ToolShell} 外壳（标题 + 说明 + 主操作 + 状态条），
 * 子类只需在 build 中向 shell 添加内容与按钮。由 ToolRegistry 登记后自动出现在导航树。
 */
public abstract class ToolPanel extends JPanel {

    private final String id;
    private final String title;
    private final String category;
    private final String description;
    private ToolShell shell;

    protected ToolPanel(String id, String title, String category, String description) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    /** 框架调用：构建外壳并委托子类填充内容。 */
    public final void init() {
        shell = new ToolShell(title, description);
        setLayout(new java.awt.BorderLayout());
        add(shell, java.awt.BorderLayout.CENTER);
        build(shell);
    }

    /** 子类在此向 shell 添加输入控件、按钮与内容。 */
    protected abstract void build(ToolShell shell);

    /**
     * 填入示例数据并触发默认操作（若有）。供截图/演示使用。
     * 默认实现不做任何事；带示例的工具可覆盖此方法。
     */
    public void seedDemo() {
        // 默认空实现
    }
}
