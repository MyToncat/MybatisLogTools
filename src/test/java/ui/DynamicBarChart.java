package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class DynamicBarChart extends JPanel {
    private int[] data; // 柱状图数据
    private Timer timer;

    public DynamicBarChart() {
        data = new int[10]; // 初始化柱状图数据为10个随机值
        Random random = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt(100);
        }

        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateData(); // 每秒更新一次数据
                repaint(); // 重新绘制柱状图
            }
        });
        timer.start();
    }

    private void updateData() {
        Random random = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt(100); // 更新数据为随机值
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int barWidth = getWidth() / data.length;

        for (int i = 0; i < data.length; i++) {
            int barHeight = (int) ((double) getHeight() * data[i] / getMaxValue(data)); // 计算柱形高度
            int x = i * barWidth;
            int y = getHeight() - barHeight;
            g2d.setColor(Color.BLUE); // 设置柱形颜色
            g2d.fillRect(x, y, barWidth - 5, barHeight); // 绘制柱形
        }

        g2d.dispose();
    }

    private int getMaxValue(int[] data) {
        int max = Integer.MIN_VALUE;
        for (int value : data) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dynamic Bar Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        DynamicBarChart dynamicBarChart = new DynamicBarChart();
        frame.add(dynamicBarChart);

        frame.setVisible(true);
    }
}