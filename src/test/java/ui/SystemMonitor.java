package ui;

import javax.swing.*;
import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Random;

public class SystemMonitor extends JFrame {
    private JLabel cpuLabel;
    private JLabel memoryLabel;

    public SystemMonitor() {
        super("System Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);

        cpuLabel = new JLabel("CPU Usage: ");
        memoryLabel = new JLabel("Memory Usage: ");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(cpuLabel);
        panel.add(memoryLabel);

        add(panel);

        Timer timer = new Timer(1000, e -> updateSystemInfo());
        timer.start();
    }

    private void updateSystemInfo() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

        double cpuUsage = (new Random()).nextDouble();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;

        cpuLabel.setText("CPU Usage: " + String.format("%.2f%%", cpuUsage));
        memoryLabel.setText("Memory Usage: " + formatBytes(usedMemory) + " / " + formatBytes(totalMemory));
    }

    private String formatBytes(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SystemMonitor systemMonitor = new SystemMonitor();
            systemMonitor.setVisible(true);
        });
    }
}