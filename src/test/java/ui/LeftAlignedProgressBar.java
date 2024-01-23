package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class LeftAlignedProgressBar extends JPanel {
    private int progress; // 进度数据

    public LeftAlignedProgressBar() {
        this.progress = 0;
        Timer timer = new Timer(1000, e -> {
            Random random = new Random();
            int newValue = random.nextInt(101); // 随机生成 0 到 100 之间的数值
            setProgress(newValue);
        });
        timer.start();
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

        // 计算进度条的宽度
        int progressWidth = (int) ((double) width * progress / 100);

        // 绘制进度条底部背景
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, width, height);

        // 绘制进度条
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, progressWidth, height);

        // 在进度条上显示进度数字（靠左显示）
        g2d.setColor(Color.BLACK);
        String progressText = progress + "%";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(progressText);
        int textHeight = fm.getHeight();
        g2d.drawString(progressText, 5, height / 2 + textHeight / 2 - 2); // 修改 x 坐标位置

        g2d.dispose();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Left-Aligned Progress Bar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 50);

        LeftAlignedProgressBar progressBar = new LeftAlignedProgressBar();
        frame.add(progressBar);

        frame.setVisible(true);
    }
}