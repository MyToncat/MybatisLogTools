package com.pqq.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  json 按照key排序
 */
public class JsonSortByKeyListener implements ActionListener {

    JTextPane textPane;
    ObjectMapper objectMapper;

    public JsonSortByKeyListener(JTextPane textPane) {
        this.textPane = textPane;
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = textPane.getText();
        if (!StringUtils.isBlank(text)) {
            try {
                Object o = objectMapper.readValue(text, Object.class);
                String minifiedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
                textPane.setText(minifiedJson);
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        }
    }
}
