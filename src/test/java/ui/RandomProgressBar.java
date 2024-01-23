package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RandomProgressBar {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Random Progress Bar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);

        // 创建水平方向的进度条
        JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        progressBar.setStringPainted(true); // 显示进度文字
        progressBar.setForeground(Color.BLUE); // 设置前景色

        // 定时器每秒更新进度
        Timer timer = new Timer(1000, e -> {
            Random random = new Random();
            int newValue = random.nextInt(progressBar.getMaximum() + 1); // 生成 0 到最大值之间的随机数
            progressBar.setValue(newValue); // 设置进度值
            progressBar.setString(newValue + "/" + progressBar.getMaximum()); // 在右侧显示当前值和最大值
        });
        timer.start();

        frame.add(progressBar, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}