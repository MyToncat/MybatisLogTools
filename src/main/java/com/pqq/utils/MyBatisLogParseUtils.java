package com.pqq.utils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class MyBatisLogParseUtils {
    private static final char MARK = '?';

    private static final Set<String> NEED_BRACKETS;

    private String sql = null;

    static {
        Set<String> types = new HashSet<>(8);
        types.add("String");
        types.add("Date");
        types.add("Time");
        types.add("LocalDate");
        types.add("LocalTime");
        types.add("LocalDateTime");
        types.add("BigDecimal");
        types.add("Timestamp");
        NEED_BRACKETS = Collections.unmodifiableSet(types);
    }


    /**
     * 解析日志sql
     * @param sql
     * @param params
     * @return
     */
    public static StringBuilder parseSql(String sql, Queue<Map.Entry<String, String>> params) {

        final StringBuilder sb = new StringBuilder(sql);

        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) != MARK) {
                continue;
            }

            final Map.Entry<String, String> entry = params.poll();
            if (Objects.isNull(entry)) {
                continue;
            }

            sb.deleteCharAt(i);

            if (NEED_BRACKETS.contains(entry.getValue())) {
                sb.insert(i, String.format("'%s'", entry.getKey()));
            } else {
                sb.insert(i, entry.getKey());
            }


        }

        return sb;
    }

    /**
     * 解析日志参数
     * @param line
     * @return
     */
    public static Queue<Map.Entry<String, String>> parseParams(String line) {
        line = StringUtils.removeEnd(line, "\n");

        final String[] strings = StringUtils.splitByWholeSeparator(line, ", ");
        final Queue<Map.Entry<String, String>> queue = new ArrayDeque<>(strings.length);

        for (String s : strings) {
            String value = StringUtils.substringBeforeLast(s, "(");
            String type = StringUtils.substringBetween(s, "(", ")");
            if (StringUtils.isEmpty(type)) {
                queue.offer(new AbstractMap.SimpleEntry<>(value, null));
            } else {
                queue.offer(new AbstractMap.SimpleEntry<>(value, type));
            }
        }

        return queue;
    }

}
