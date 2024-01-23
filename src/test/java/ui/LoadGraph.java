package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class LoadGraph extends JPanel {
    private int[] loadHistory; // 存储负载历史数据
    private int historySize = 100; // 存储负载数据的历史记录长度
    private Random random = new Random();

    public LoadGraph() {
        loadHistory = new int[historySize];
        for (int i = 0; i < historySize; i++) {
            loadHistory[i] = random.nextInt(100); // 初始化随机负载数据
        }

        Timer timer = new Timer(1000, e -> updateLoad());
        timer.start();
    }

    private void updateLoad() {
        for (int i = 0; i < historySize - 1; i++) {
            loadHistory[i] = loadHistory[i + 1]; // 移动历史数据
        }
        loadHistory[historySize - 1] = random.nextInt(100); // 生成新的随机负载数据
        repaint(); // 重新绘制图形
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / historySize;

        for (int i = 0; i < historySize; i++) {
            int x = i * barWidth;
            int barHeight = (int) ((double) height * loadHistory[i] / 100);
            int y = height - barHeight;
            g2d.setColor(Color.BLUE); // 设置负载柱形颜色
            g2d.fillRect(x, y, barWidth - 1, barHeight);
        }

        g2d.dispose();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Load Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        LoadGraph loadGraph = new LoadGraph();
        frame.add(loadGraph);

        frame.setVisible(true);
    }
}