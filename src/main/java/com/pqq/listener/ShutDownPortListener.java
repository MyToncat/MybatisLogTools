package com.pqq.listener;

import cn.hutool.core.stream.CollectorUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RuntimeUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShutDownPortListener implements ActionListener {
    private JTextField portText;
    private String cmd= "netstat -ano";

    public ShutDownPortListener(JTextField portText) {
        this.portText = portText;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String port = portText.getText();
        if (StringUtils.isEmpty(port) || StringUtils.isEmpty(port.trim())) {
            return;
        }
        String[] portList = port.split(",");
        // 支持持window
        if (!(File.separatorChar == '\\')) {
            return;
        }
        try {
            // 获取执行结果
            String[] resultStr = Optional.ofNullable(RuntimeUtil.execForStr(cmd))
                    .map(ee -> {
                        return ee.trim().split("\n");
                    }).orElse(new String[]{});

            // 获取pid
            List<String> pidList = Stream.of(resultStr).filter(ee -> {
                if (StringUtils.isEmpty(ee) || !ee.contains(ee)) {
                    return false;
                }
                // 格式
                return true;
            }).map(ee -> {
                String[] str = ee.trim().split("\\s+");
                // 有些没有监听状态，长度不一定是5
                if (!Objects.isNull(str) && str.length >= 2 && str[1].contains(":" + port)) {
                    return str[str.length - 1];
                }
                return null;
            }).filter(ee -> {
                return ee != null;
            }).distinct().collect(Collectors.toList());

            if (pidList == null || pidList.size() == 0) {
                JOptionPane.showMessageDialog(null, "端口"+ port + "查询不到占用" , "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (String pid : pidList) {
                String s = RuntimeUtil.execForStr("taskkill /F /PID " + pid);
                if (StringUtils.contains("原因",s)) {
                    JOptionPane.showMessageDialog(null, "成功关闭端口:" + port , "异常错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "关闭端口" + port + "失败 " + s, "异常错误", JOptionPane.ERROR_MESSAGE);

                }
            }
         } catch (Exception ee){
            System.out.println(ee.getMessage());
            JOptionPane.showMessageDialog(null, ee.getCause(), "异常错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}

