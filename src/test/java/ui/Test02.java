package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Test02 extends JPanel {
    private int progress; // 进度数据

    public Test02(int progress) {
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

        Random random = new Random();
        int i = random.nextInt();
        // 计算进度条的宽度
        int progressWidth = (int) ((double) width * i / 100);

        // 绘制进度条底部背景
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, width, height);

        // 绘制进度条
        g2d.setColor(Color.pink);
        g2d.fillRect(0, 0, progressWidth, height);

        // 在进度条上显示进度数字
        g2d.setColor(Color.BLACK);
        g2d.drawString(progress + "%", width / 2 - 15, height / 2 + 5);

        g2d.dispose();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Progress Bar Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 50);

        Test02 progressBarGraph = new Test02(50); // 初始进度为50%
        frame.add(progressBarGraph);

        // 模拟进度更新
        Timer timer = new Timer(1000, e -> {
            int newProgress = progressBarGraph.progress + 10;
            if (newProgress > 100) {
                newProgress = 0;
            }
            progressBarGraph.setProgress(newProgress);
        });
        timer.start();

        frame.setVisible(true);
    }
}