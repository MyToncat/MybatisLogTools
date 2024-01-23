package ui;

import javax.swing.*;
import java.awt.*;

public class BasicGraphicsExample extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 设置绘图颜色
        g2d.setColor(Color.RED);

        // 绘制矩形边框
        g2d.drawRect(50, 50, 100, 50);

        // 设置绘图颜色
        g2d.setColor(Color.BLUE);

        // 绘制填充的矩形
        g2d.fillRect(200, 50, 100, 50);

        // 设置绘图颜色
        g2d.setColor(Color.GREEN);

        // 绘制椭圆边框
        g2d.drawOval(50, 150, 100, 50);

        // 设置绘图颜色
        g2d.setColor(Color.ORANGE);

        // 绘制填充的椭圆
        g2d.fillOval(200, 150, 100, 50);

        // 设置绘图颜色
        g2d.setColor(Color.MAGENTA);

        // 绘制直线
        g2d.drawLine(50, 250, 300, 250);

        // 设置绘图颜色
        g2d.setColor(Color.CYAN);

        // 绘制弧线边框
        g2d.drawArc(50, 300, 100, 100, 30, 120);

        // 设置绘图颜色
        g2d.setColor(Color.YELLOW);

        // 绘制填充的弧线
        g2d.fillArc(200, 300, 100, 100, 45, 90);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Basic Graphics Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 450);

        BasicGraphicsExample panel = new BasicGraphicsExample();
        frame.add(panel);

        frame.setVisible(true);
    }
}