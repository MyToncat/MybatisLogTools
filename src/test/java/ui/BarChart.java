package ui;

import javax.swing.*;
import java.awt.*;

public class BarChart extends JPanel {
    private int[] data; // 柱状图数据

    public BarChart(int[] data) {
        this.data = data;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int barWidth = getWidth() / data.length;

        for (int i = 0; i < data.length; i++) {
            int barHeight = (int) ((double) getHeight() * data[i] / getMaxValue(data)); // 根据数据计算柱形高度
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
        int[] sampleData = {30, 60, 90, 120, 45}; // 示例数据

        JFrame frame = new JFrame("Bar Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        BarChart barChart = new BarChart(sampleData);
        frame.add(barChart);

        frame.setVisible(true);
    }
}