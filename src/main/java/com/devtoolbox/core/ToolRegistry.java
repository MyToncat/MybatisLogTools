package com.devtoolbox.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具注册表。所有工具在 MainFrame 中通过 register 登记，
 * 框架据此按分类构建左侧导航树。
 */
public final class ToolRegistry {

    private static final List<ToolPanel> TOOLS = new ArrayList<>();

    private ToolRegistry() {
    }

    public static void register(ToolPanel panel) {
        TOOLS.add(panel);
    }

    public static List<ToolPanel> getAll() {
        return new ArrayList<>(TOOLS);
    }

    /** 按分类分组，保持注册顺序。 */
    public static Map<String, List<ToolPanel>> groupByCategory() {
        Map<String, List<ToolPanel>> groups = new LinkedHashMap<>();
        for (ToolPanel p : TOOLS) {
            groups.computeIfAbsent(p.getCategory(), k -> new ArrayList<>()).add(p);
        }
        return groups;
    }
}
