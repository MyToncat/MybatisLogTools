package com.pqq.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pqq.utils.JsonFormatUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  缩进json
 */
public class JsonMinigyListener implements ActionListener {

    JTextPane textPane;
    ObjectMapper objectMapper;

    public JsonMinigyListener(JTextPane textPane) {
        this.textPane = textPane;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = textPane.getText();
        if (!StringUtils.isBlank(text)) {
            try {
                String minifiedJson = JsonFormatUtils.minifyJson(text);
                textPane.setText(minifiedJson);
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        }
    }
}
