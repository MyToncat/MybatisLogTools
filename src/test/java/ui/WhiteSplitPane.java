package ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

public class WhiteSplitPane {
    public static void main(String[] args) {
        JFrame frame = new JFrame("White SplitPane");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(new JPanel());
        splitPane.setRightComponent(new JPanel());

        // 获取分隔条的 UI
        BasicSplitPaneUI splitPaneUI = (BasicSplitPaneUI) splitPane.getUI();
        // 设置分隔条为白色
        splitPaneUI.getDivider().setBackground(Color.RED);

        frame.add(splitPane);
        frame.setVisible(true);
    }
}