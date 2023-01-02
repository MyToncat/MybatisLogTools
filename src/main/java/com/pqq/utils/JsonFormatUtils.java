package com.pqq.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *   json格式话工具
 */
public class JsonFormatUtils {
    private JsonFormatUtils() {
    }

    /**
     * 格式化
     * @param jsonStr
     * @return
     * @throws JsonProcessingException
     */
    public static String formatJson(String jsonStr) throws JsonProcessingException {
        Object jsonObject = JsonFormatUtils.Holder.MAPPER.readValue(jsonStr, Object.class);
        return JsonFormatUtils.Holder.MAPPER.writer(JsonFormatUtils.Holder.DEFAULT_PRETTY_PRINTER).writeValueAsString(jsonObject);
    }

    /**
     * 缩进
     * @param jsonStr
     * @return
     * @throws JsonProcessingException
     */
    public static String minifyJson(String jsonStr) throws JsonProcessingException {
        Object jsonObject = JsonFormatUtils.Holder.MAPPER.readValue(jsonStr, Object.class);
        return JsonFormatUtils.Holder.MAPPER.writeValueAsString(jsonObject);
    }

    public static void verifyJson(String jsonStr) throws JsonProcessingException {
        JsonFormatUtils.Holder.MAPPER.readValue(jsonStr, Object.class);
    }

    /**
     *   格式化
     */
    private static final class CustomPrettyPrinter extends DefaultPrettyPrinter {
        // 定义 缩进符号 和换行符号
        private static final DefaultIndenter UNIX_LINE_FEED_INSTANCE = new DefaultIndenter("  ", "\n");

        public CustomPrettyPrinter() {
            super._objectFieldValueSeparatorWithSpaces = ": ";
            super._objectIndenter = UNIX_LINE_FEED_INSTANCE;
            super._arrayIndenter = UNIX_LINE_FEED_INSTANCE;
        }

        public DefaultPrettyPrinter createInstance() {
            return new JsonFormatUtils.CustomPrettyPrinter();
        }
    }

    /**
     *  持有者
     */
    private static final class Holder {
        public static final ObjectMapper MAPPER = new ObjectMapper();
        public static final DefaultPrettyPrinter DEFAULT_PRETTY_PRINTER = new JsonFormatUtils.CustomPrettyPrinter();

        private Holder() {
        }
    }
}