package com.devtoolbox.tools;

import com.devtoolbox.core.MainFrame;
import com.devtoolbox.core.ToolPanel;
import com.formdev.flatlaf.FlatLightLaf;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;

/**
 * 一次性截图驱动器：构建主窗口、依次选中各工具，
 * 用 Swing 离屏渲染（paintImmediately 同步绘制整个内容面板）保存 PNG。
 * 用作 README 配图生成。
 *
 * 用法：java com.devtoolbox.tools.ScreenshotApp [outDir]
 */
public class ScreenshotApp {

    public static void main(String[] args) throws Exception {
        String outDir = args.length > 0 ? args[0] : "docs/screenshots";
        new File(outDir).mkdirs();

        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignore) {
        }

        String[] shots = {
            "__main__", "JSON 解析", "Base64 工具", "MD5/SHA 哈希", "URL 编解码",
            "UUID 生成", "时间戳转换", "颜色转换 / 拾取", "Crontab 解析", "CSV 转 SQL"
        };

        final MainFrame frame = new MainFrame();
        SwingUtilities.invokeAndWait(() -> {
            frame.initRegistry();
            frame.initUI();
            // 真实显示并移到屏幕外，确保 Swing 完成布局与字体度量
            frame.setSize(1180, 760);
            frame.setLocation(-3000, -3000);
            frame.setVisible(true);
        });

        // 等待首帧绘制完成
        Thread.sleep(1500);

        for (int idx = 0; idx < shots.length; idx++) {
            final String name = shots[idx];
            if (!"__main__".equals(name)) {
                SwingUtilities.invokeAndWait(() -> {
                    frame.selectTool(name);
                    frame.seedCurrentTool();
                });
                // 给示例数据填充 + 重绘留时间
                Thread.sleep(1200);
            }

            // 在 EDT 中同步渲染整窗口到 BufferedImage
            final BufferedImage[] out = new BufferedImage[1];
            SwingUtilities.invokeAndWait(() -> {
                out[0] = frame.renderToImage();
            });
            BufferedImage img = out[0];

            String file;
            if ("__main__".equals(name)) {
                file = "01-main.png";
            } else {
                file = String.format("%02d-%s.png", idx + 1, name).replaceAll("[\\\\/:*?\"<>|]", "_");
            }
            File f = new File(outDir, file);
            ImageIO.write(img, "png", f);
            System.out.println("saved " + f.getAbsolutePath() + "  (" + img.getWidth() + "x" + img.getHeight() + ")");
        }

        SwingUtilities.invokeAndWait(() -> frame.dispose());
        System.exit(0);
    }
}