package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class StyledProgressBar extends JPanel {
    private int progress; // 进度数据



    public StyledProgressBar(int progress) {
        this.progress = progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

         int width = getWidth();
         int height = getHeight();

//        int width = getWidth();
//        int height = getHeight();

        // 计算进度条的宽度
        int progressWidth = (int) ((double) width * progress / 100);

        // 绘制进度条背景（灰色）
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, width, height);

        // 绘制进度条底部边框
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRect(0, 0, width - 1, height - 1);

        // 创建渐变颜色
        GradientPaint gradient = new GradientPaint(0, 0, Color.orange, progressWidth, height, Color.pink);
        g2d.setPaint(gradient);

        // 绘制填充的进度条
        g2d.fillRect(0, 0, progressWidth, height);

        // 在进度条上显示进度数字（靠左显示）
        g2d.setColor(Color.BLACK);
        String progressText = progress + "%";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(progressText);
        int textHeight = fm.getHeight();
        g2d.drawString(progressText, 5 + textWidth, height / 2 + textHeight / 2 - 2); // 修改 x 坐标位置

        g2d.dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Styled Progress Bar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 50);

        StyledProgressBar progressBar = new StyledProgressBar(0); // 初始进度为0%
        frame.add(progressBar);

        // 模拟随机进度更新
        Timer timer = new Timer(1000, e -> {
            Random random = new Random();
            int newProgress = random.nextInt(101); // 生成 0 到 100 之间的随机数作为进度
            progressBar.setProgress(newProgress);
        });
        timer.start();

        frame.setVisible(true);
    }
}