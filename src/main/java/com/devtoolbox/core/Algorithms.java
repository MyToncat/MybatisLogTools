package com.devtoolbox.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 各工具核心算法的纯函数实现，便于复用与单元测试。
 */
public final class Algorithms {

    public static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Algorithms() {
    }

    // ---- JSON ----
    public static String jsonFormat(String text) {
        JsonElement el = JsonParser.parseString(text);
        return new GsonBuilder().setPrettyPrinting().create().toJson(el);
    }

    public static String jsonMinify(String text) {
        return JsonParser.parseString(text).toString();
    }

    public static boolean jsonValid(String text) {
        try {
            JsonParser.parseString(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ---- Base64 ----
    public static String base64Encode(String text, String charset, boolean urlSafe) {
        byte[] bytes = text.getBytes(Charset.forName(charset));
        return (urlSafe ? java.util.Base64.getUrlEncoder() : java.util.Base64.getEncoder()).encodeToString(bytes);
    }

    public static String base64Decode(String text, String charset, boolean urlSafe) {
        byte[] decoded = (urlSafe ? java.util.Base64.getUrlDecoder() : java.util.Base64.getDecoder()).decode(text.trim());
        return new String(decoded, Charset.forName(charset));
    }

    // ---- Hash ----
    public static String hash(String algo, String text, boolean upper) {
        try {
            MessageDigest md = MessageDigest.getInstance(algo.replace("-", ""));
            byte[] digest = md.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            String hex = sb.toString();
            return upper ? hex.toUpperCase() : hex;
        } catch (Exception e) {
            throw new IllegalArgumentException("不支持的算法: " + algo, e);
        }
    }

    // ---- URL ----
    public static String urlEncode(String text) {
        try {
            return java.net.URLEncoder.encode(text, "UTF-8");
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String urlDecode(String text) {
        try {
            return java.net.URLDecoder.decode(text, "UTF-8");
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    // ---- UUID ----
    public static List<String> uuid(int count, boolean noDash, boolean upper) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String u = UUID.randomUUID().toString();
            if (noDash) u = u.replace("-", "");
            if (upper) u = u.toUpperCase();
            list.add(u);
        }
        return list;
    }

    // ---- Timestamp ----
    public static String tsToDate(long ts, boolean isSeconds, ZoneId zone) {
        long millis = isSeconds ? ts * 1000 : ts;
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), zone).format(TS_FMT);
    }

    public static long dateToTs(String date, boolean isSeconds, ZoneId zone) {
        LocalDateTime ldt = LocalDateTime.parse(date.trim(), TS_FMT);
        long millis = ldt.atZone(zone).toInstant().toEpochMilli();
        return isSeconds ? millis / 1000 : millis;
    }

    // ---- CSV -> SQL ----
    /**
     * 将 CSV 文本转换为 INSERT 语句。
     *
     * @param csv         CSV 文本（UTF-8，多行）
     * @param tableName   目标表名
     * @param delimiter   字段分隔符，默认 ','
     * @param hasHeader   首行是否为列名（作为 INSERT 列名）
     * @param quoteAll    为 true 时所有值加单引号；为 false 时仅非数字/非 null 值加引号
     * @param insertPerRow 为 true 时每行一条 INSERT；为 false 时合并为单条多值 INSERT
     */
    public static String csvToSql(String csv, String tableName, char delimiter,
                                  boolean hasHeader, boolean quoteAll, boolean insertPerRow) {
        if (csv == null || csv.trim().isEmpty()) {
            throw new IllegalArgumentException("CSV 内容为空");
        }
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new IllegalArgumentException("表名不能为空");
        }
        List<String[]> rows = parseCsv(csv, delimiter);
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("CSV 无有效数据行");
        }

        String[] columns;
        int start;
        if (hasHeader) {
            columns = rows.get(0);
            start = 1;
        } else {
            int n = rows.get(0).length;
            columns = new String[n];
            for (int i = 0; i < n; i++) {
                columns[i] = "col" + (i + 1);
            }
            start = 0;
        }

        StringBuilder sb = new StringBuilder();
        String colList = String.join(", ", columns);
        if (insertPerRow) {
            for (int r = start; r < rows.size(); r++) {
                String[] row = rows.get(r);
                sb.append("INSERT INTO ").append(tableName).append(" (").append(colList).append(") VALUES (")
                        .append(valuesClause(row, quoteAll)).append(");\n");
            }
        } else {
            sb.append("INSERT INTO ").append(tableName).append(" (").append(colList).append(") VALUES\n");
            List<String> tuples = new ArrayList<>();
            for (int r = start; r < rows.size(); r++) {
                tuples.add("  (" + valuesClause(rows.get(r), quoteAll) + ")");
            }
            sb.append(String.join(",\n", tuples)).append(";");
        }
        return sb.toString();
    }

    private static String valuesClause(String[] row, boolean quoteAll) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row.length; i++) {
            if (i > 0) sb.append(", ");
            String v = row[i];
            if (v == null) v = "";
            if (quoteAll || !isNumericOrNull(v)) {
                sb.append("'").append(v.replace("'", "''")).append("'");
            } else {
                sb.append(v);
            }
        }
        return sb.toString();
    }

    private static boolean isNumericOrNull(String v) {
        if (v.isEmpty()) return true;
        try {
            Double.parseDouble(v);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** 解析一行 CSV，支持双引号包裹与字段内逗号/换行转义（"" 表示引号本身）。 */
    public static List<String[]> parseCsvForCheck(String csv, char delimiter) { return parseCsv(csv, delimiter); }

    private static List<String[]> parseCsv(String csv, char delimiter) {
        List<String[]> rows = new ArrayList<>();
        List<String> fields = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        String text = csv.replace("\r\n", "\n");
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < text.length() && text.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == delimiter) {
                    fields.add(cur.toString());
                    cur.setLength(0);
                } else if (c == '\n') {
                    fields.add(cur.toString());
                    rows.add(fields.toArray(new String[0]));
                    fields.clear();
                    cur.setLength(0);
                } else {
                    cur.append(c);
                }
            }
        }
        // 最后一行
        fields.add(cur.toString());
        rows.add(fields.toArray(new String[0]));
        // 去掉完全空白的尾行
        rows.removeIf(r -> r.length == 1 && r[0].trim().isEmpty());
        return rows;
    }

    // ---- Color ----
    public static int[] hexToRgb(String hex) {
        String h = hex.trim().replace("#", "");
        if (h.length() == 3) {
            h = "" + h.charAt(0) + h.charAt(0) + h.charAt(1) + h.charAt(1) + h.charAt(2) + h.charAt(2);
        }
        java.awt.Color c = new java.awt.Color(Integer.parseInt(h, 16));
        return new int[]{c.getRed(), c.getGreen(), c.getBlue()};
    }

    public static String rgbToHex(int r, int g, int b) {
        return String.format("#%02X%02X%02X",
                Math.max(0, Math.min(255, r)),
                Math.max(0, Math.min(255, g)),
                Math.max(0, Math.min(255, b)));
    }

    // ---- Cron ----
    public static String cronDescribe(String expr) {
        String[] p = expr.trim().split("\\s+");
        if (p.length != 5) {
            throw new IllegalArgumentException("需 5 段");
        }
        return "每天 " + field(p[1], "时", 0, 23) + ":"
                + pad(field(p[0], "分", 0, 59)) + "，月份："
                + field(p[3], "月", 1, 12) + "，日期："
                + field(p[2], "日", 1, 31) + "，星期："
                + field(p[4], "星期", 0, 7).replace("7", "0");
    }

    private static String pad(String s) {
        return s.length() == 1 ? "0" + s : s;
    }

    private static String field(String f, String unit, int min, int max) {
        if (f.equals("*")) return "每" + unit;
        if (f.contains("/")) {
            String[] sp = f.split("/");
            return "从 " + (sp[0].equals("*") ? min : sp[0]) + " 起每 " + sp[1] + " " + unit;
        }
        if (f.contains("-")) {
            String[] sp = f.split("-");
            return sp[0] + " 至 " + sp[1] + " " + unit;
        }
        if (f.contains(",")) return f.replace(",", "、") + " " + unit;
        return f + " " + unit;
    }
}
