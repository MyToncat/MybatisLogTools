package com.devtoolbox.core;

import com.devtoolbox.tools.Base64Tool;
import com.devtoolbox.tools.ColorTool;
import com.devtoolbox.tools.CronTool;
import com.devtoolbox.tools.CsvToSqlTool;
import com.devtoolbox.tools.HashTool;
import com.devtoolbox.tools.JsonTool;
import com.devtoolbox.tools.TimestampTool;
import com.devtoolbox.tools.UrlTool;
import com.devtoolbox.tools.UuidTool;

import com.devtoolbox.core.Ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

/**
 * 主窗口：左侧分类导航树 + 右侧工具工作区。
 */
public class MainFrame extends JFrame implements TreeSelectionListener {

    private final Map<String, ToolPanel> panelCache = new HashMap<>();
    private final CardWorkspace workspace = new CardWorkspace();
    private JTree tree;

    public MainFrame() {
        super("DevToolbox - 开发者工具箱");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
    }

    /** 登记所有工具。新增工具时在此处 register 即可出现在导航树。 */
    public void initRegistry() {
        ToolRegistry.register(new JsonTool());
        ToolRegistry.register(new Base64Tool());
        ToolRegistry.register(new HashTool());
        ToolRegistry.register(new UrlTool());
        ToolRegistry.register(new UuidTool());
        ToolRegistry.register(new TimestampTool());
        ToolRegistry.register(new ColorTool());
        ToolRegistry.register(new CronTool());
        ToolRegistry.register(new CsvToSqlTool());
    }

    public void initUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));

        tree = buildTree();
        JScrollPane treeScroll = new JScrollPane(tree);
        treeScroll.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Ui.BORDER));
        treeScroll.setBackground(Ui.PANEL);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScroll, workspace);
        split.setDividerLocation(230);
        split.setBorder(null);
        split.setContinuousLayout(true);

        // 顶部应用标题栏
        JLabel appTitle = new JLabel("  开发者工具箱");
        appTitle.setFont(new java.awt.Font("Microsoft YaHei", java.awt.Font.BOLD, 16));
        appTitle.setForeground(Color.WHITE);
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Ui.PRIMARY);
        topBar.setPreferredSize(new Dimension(0, 44));
        topBar.add(appTitle, BorderLayout.WEST);

        root.add(topBar, BorderLayout.NORTH);
        root.add(split, BorderLayout.CENTER);
        setContentPane(root);

        // 默认展开分类并选中第一个工具，避免空白页
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        DefaultMutableTreeNode firstCat = (DefaultMutableTreeNode) tree.getModel().getChild(tree.getModel().getRoot(), 0);
        if (firstCat != null && firstCat.getChildCount() > 0) {
            DefaultMutableTreeNode firstLeaf = (DefaultMutableTreeNode) firstCat.getChildAt(0);
            tree.setSelectionPath(new javax.swing.tree.TreePath(firstLeaf.getPath()));
        }
    }

    private JTree buildTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("工具箱");
        Map<String, DefaultMutableTreeNode> catNodes = new HashMap<>();
        for (Map.Entry<String, java.util.List<ToolPanel>> entry : ToolRegistry.groupByCategory().entrySet()) {
            DefaultMutableTreeNode catNode = new DefaultMutableTreeNode(entry.getKey());
            for (ToolPanel p : entry.getValue()) {
                catNode.add(new DefaultMutableTreeNode(p.getTitle()));
                panelCache.put(p.getTitle(), p);
            }
            catNodes.put(entry.getKey(), catNode);
            root.add(catNode);
        }
        JTree t = new JTree(new DefaultTreeModel(root));
        t.addTreeSelectionListener(this);
        t.setRootVisible(true);
        t.setShowsRootHandles(true);
        t.setBackground(Ui.PANEL);
        t.setRowHeight(26);
        t.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
        return t;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        Object node = tree.getLastSelectedPathComponent();
        if (node == null || !(node instanceof DefaultMutableTreeNode)) {
            return;
        }
        DefaultMutableTreeNode sel = (DefaultMutableTreeNode) node;
        if (sel.isLeaf()) {
            ToolPanel panel = panelCache.get(sel.getUserObject().toString());
            if (panel != null) {
                if (!workspace.has(panel.getId())) {
                    panel.init();
                    workspace.add(panel, panel.getId());
                }
                workspace.show(panel.getId());
            }
        }
    }

    /**
     * 按工具标题选中并显示对应工具面板（供截图/测试使用）。
     * 若标题不存在则忽略。
     */
    public void selectTool(String title) {
        if (tree == null) {
            return;
        }
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode cat = (DefaultMutableTreeNode) root.getChildAt(i);
            for (int j = 0; j < cat.getChildCount(); j++) {
                DefaultMutableTreeNode leaf = (DefaultMutableTreeNode) cat.getChildAt(j);
                if (title.equals(leaf.getUserObject().toString())) {
                    tree.expandPath(new javax.swing.tree.TreePath(cat.getPath()));
                    tree.setSelectionPath(new javax.swing.tree.TreePath(leaf.getPath()));
                    return;
                }
            }
        }
    }

    /**
     * 对当前选中的工具调用 seedDemo()，填入示例并触发默认操作。
     * 用于截图 / 演示。调用前应已通过 selectTool 完成构建。
     */
    public void seedCurrentTool() {
        javax.swing.tree.TreePath path = tree.getSelectionPath();
        if (path == null) {
            return;
        }
        Object leaf = path.getLastPathComponent();
        if (leaf instanceof DefaultMutableTreeNode) {
            ToolPanel panel = panelCache.get(((DefaultMutableTreeNode) leaf).getUserObject().toString());
            if (panel != null) {
                panel.seedDemo();
            }
        }
    }

    /**
     * 将当前主窗口内容渲染到离屏 BufferedImage。
     * 调用方必须处于 Swing EDT 线程；返回的图像尺寸为当前 JFrame 实际尺寸。
     */
    public java.awt.image.BufferedImage renderToImage() {
        java.awt.Dimension size = getRootPane().getSize();
        if (size.width <= 0 || size.height <= 0) {
            size = new java.awt.Dimension(1100, 720);
        }
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                size.width, size.height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2 = img.createGraphics();
        try {
            // 同步绘制，确保离屏图像完整（不依赖 Swing 异步重绘）
            getRootPane().paintImmediately(0, 0, size.width, size.height);
            getRootPane().paint(g2);
        } finally {
            g2.dispose();
        }
        return img;
    }

    /** 卡片式工作区：每个工具仅初始化一次，切换时不丢失状态。 */
    private static final class CardWorkspace extends JPanel {
        private final java.awt.CardLayout layout = new java.awt.CardLayout();
        private final Map<String, ToolPanel> cards = new HashMap<>();

        CardWorkspace() {
            super();
            setLayout(layout);
            JPanel empty = new JPanel(new BorderLayout());
            empty.add(new javax.swing.JLabel("请从左侧选择一个工具", javax.swing.SwingConstants.CENTER),
                    BorderLayout.CENTER);
            add(empty, "__empty__");
        }

        boolean has(String id) {
            return cards.containsKey(id);
        }

        void add(ToolPanel panel, String id) {
            cards.put(id, panel);
            super.add(panel, id);
        }

        void show(String id) {
            layout.show(this, id);
        }
    }
}
