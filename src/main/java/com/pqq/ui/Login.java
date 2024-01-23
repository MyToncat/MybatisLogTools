/*
 * Created by JFormDesigner on Wed Dec 27 22:44:11 CST 2023
 */

package com.pqq.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author Administrator
 */
public class Login extends JFrame {
    public Login() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("conf.myFLatLef");
        label1 = new JLabel();
        textField1 = new JTextField();
        label2 = new JLabel();
        textField2 = new JTextField();
        hSpacer1 = new JPanel(null);
        button1 = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("\u767b\u5f55");
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "novisualpadding,align right center",
            // columns
            "[75,center]" +
            "[253,fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[4]"));

        //---- label1 ----
        label1.setText(bundle.getString("label1.text"));
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setHorizontalTextPosition(SwingConstants.CENTER);
        contentPane.add(label1, "cell 0 1,alignx trailing,growx 0");

        //---- textField1 ----
        textField1.setMinimumSize(new Dimension(160, 24));
        contentPane.add(textField1, "cell 1 1,alignx leading,growx 0");

        //---- label2 ----
        label2.setText(bundle.getString("label2.text"));
        contentPane.add(label2, "cell 0 2,alignx right,growx 0");

        //---- textField2 ----
        textField2.setMinimumSize(new Dimension(160, 24));
        contentPane.add(textField2, "cell 1 2,alignx leading,growx 0");
        contentPane.add(hSpacer1, "cell 1 4,growx");

        //---- button1 ----
        button1.setText(bundle.getString("button1.text"));
        contentPane.add(button1, "cell 1 4");
        setSize(410, 200);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel label1;
    private JTextField textField1;
    private JLabel label2;
    private JTextField textField2;
    private JPanel hSpacer1;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }
}
