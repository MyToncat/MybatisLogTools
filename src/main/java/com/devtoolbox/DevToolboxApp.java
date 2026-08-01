package com.devtoolbox;

import com.devtoolbox.core.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * 程序入口。初始化主题与主窗口。
 * 命令行支持：
 *   --tool <title>   启动后自动选中并填充示例数据（用于截图/演示）
 *   --x <px>         窗口 X 坐标
 *   --y <px>         窗口 Y 坐标
 *   --w <px>         窗口宽度
 *   --h <px>         窗口高度
 */
public final class DevToolboxApp {

    private DevToolboxApp() {
    }

    public static void main(String[] args) {
        String seedTool = null;
        int x = 0, y = 0, w = 1180, h = 760;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--tool":
                    if (i + 1 < args.length) seedTool = args[++i];
                    break;
                case "--x":
                    if (i + 1 < args.length) x = Integer.parseInt(args[++i]);
                    break;
                case "--y":
                    if (i + 1 < args.length) y = Integer.parseInt(args[++i]);
                    break;
                case "--w":
                    if (i + 1 < args.length) w = Integer.parseInt(args[++i]);
                    break;
                case "--h":
                    if (i + 1 < args.length) h = Integer.parseInt(args[++i]);
                    break;
                default:
            }
        }
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            // 退回系统默认外观
        }
        final int fx = x, fy = y, fw = w, fh = h;
        final String fSeedTool = seedTool;
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.initRegistry();
            frame.initUI();
            frame.setBounds(fx, fy, fw, fh);
            frame.setVisible(true);
            if (fSeedTool != null) {
                frame.selectTool(fSeedTool);
                frame.seedCurrentTool();
            }
        });
    }
}
