package ui;

import javax.swing.*;
import java.awt.*;

public class ProgressBarExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Horizontal Progress Bar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);

        // 创建水平方向的进度条
        JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        progressBar.setStringPainted(true); // 显示进度文字
        progressBar.setForeground(Color.BLUE); // 设置前景色

        // 模拟进度变化
        Timer timer = new Timer(1000, e -> {
            int value = progressBar.getValue();
            if (value < progressBar.getMaximum()) {
                progressBar.setValue(value + 10); // 增加进度值
            } else {
                ((Timer) e.getSource()).stop(); // 达到最大值后停止计时器
            }
        });
        timer.start();

        frame.add(progressBar, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}