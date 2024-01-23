package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class MyForm extends JFrame {
    private JLabel cpuLabel;

    public MyForm() {
        super("CPU Status Display");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 100);

        cpuLabel = new JLabel();
        add(cpuLabel, BorderLayout.CENTER);

        Timer timer = new Timer(1000, e -> updateCPUStatus());
        timer.start();
    }

    private void updateCPUStatus() {
        Random random = new Random();
        double cpuUsage = random.nextDouble();

        cpuLabel.setText(String.format("CPU Usage: %.2f%%", cpuUsage));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MyForm cpuStatusDisplay = new MyForm();
            cpuStatusDisplay.setVisible(true);
        });
    }
}