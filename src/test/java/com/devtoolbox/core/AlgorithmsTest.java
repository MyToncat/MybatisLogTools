package com.devtoolbox.core;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 核心算法单元测试：覆盖所有工具的关键转换逻辑。
 */
class AlgorithmsTest {

    @Test
    void jsonFormatAndValidate() {
        String src = "{\"a\":1,\"b\":[1,2,3]}";
        String pretty = Algorithms.jsonFormat(src);
        assertTrue(pretty.contains("\n"));
        assertTrue(Algorithms.jsonValid(src));
        assertFalse(Algorithms.jsonValid("{bad"));
        assertEquals(src, Algorithms.jsonMinify(src));
    }

    @Test
    void base64RoundTrip() {
        String text = "Hello, 世界!";
        String enc = Algorithms.base64Encode(text, "UTF-8", false);
        assertEquals(text, Algorithms.base64Decode(enc, "UTF-8", false));
        String url = Algorithms.base64Encode("a/b+c=", "UTF-8", true);
        assertTrue(url.contains("-") || url.contains("_") || !url.contains("/"));
        assertEquals("a/b+c=", Algorithms.base64Decode(url, "UTF-8", true));
    }

    @Test
    void hashKnownVectors() {
        assertEquals("e10adc3949ba59abbe56e057f20f883e", Algorithms.hash("MD5", "123456", false));
        assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                Algorithms.hash("SHA-256", "password", false));
        assertEquals("5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8",
                Algorithms.hash("SHA-256", "password", true));
    }

    @Test
    void urlEncodeDecode() {
        String text = "a b&c=d";
        String enc = Algorithms.urlEncode(text);
        assertEquals(text, Algorithms.urlDecode(enc));
        assertTrue(enc.contains("+"));
    }

    @Test
    void uuidGeneration() {
        List<String> list = Algorithms.uuid(5, false, false);
        assertEquals(5, list.size());
        assertEquals(36, list.get(0).length());
        String noDash = Algorithms.uuid(1, true, false).get(0);
        assertEquals(32, noDash.length());
        assertEquals(noDash, noDash.toLowerCase());
    }

    @Test
    void timestampConversion() {
        // 2024-01-01 00:00:00 UTC = 1704067200 s
        long sec = Algorithms.dateToTs("2024-01-01 00:00:00", true, ZoneId.of("UTC"));
        assertEquals(1704067200L, sec);
        String date = Algorithms.tsToDate(1704067200L, true, ZoneId.of("UTC"));
        assertEquals("2024-01-01 00:00:00", date);
    }

    @Test
    void colorConversion() {
        int[] rgb = Algorithms.hexToRgb("#FF0000");
        assertArrayEquals(new int[]{255, 0, 0}, rgb);
        int[] rgb3 = Algorithms.hexToRgb("#f00");
        assertArrayEquals(new int[]{255, 0, 0}, rgb3);
        assertEquals("#00FF00", Algorithms.rgbToHex(0, 255, 0));
        assertEquals("#FF00FF", Algorithms.rgbToHex(300, -5, 255));
    }

    @Test
    void cronDescribe() {
        String desc = Algorithms.cronDescribe("0 9 * * 1-5");
        assertTrue(desc.contains("9") && desc.contains("分"));
        assertTrue(desc.contains("1 至 5") || desc.contains("星期"));
    }

    @Test
    void csvToSqlPerRow() {
        String csv = "id,name,age\n1,张三,28\n2,李四,31";
        String sql = Algorithms.csvToSql(csv, "users", ',', true, false, true);
        assertTrue(sql.contains("INSERT INTO users (id, name, age) VALUES (1, '张三', 28);"));
        assertTrue(sql.contains("(2, '李四', 31);"));
    }

    @Test
    void csvToSqlMerged() {
        String csv = "id,name\n1,张三\n2,李四";
        String sql = Algorithms.csvToSql(csv, "users", ',', true, false, false);
        // 合并为单条多值
        assertTrue(sql.startsWith("INSERT INTO users (id, name) VALUES"));
        assertTrue(sql.contains(",\n  ("));
        assertTrue(sql.trim().endsWith(");"));
        assertTrue(sql.chars().filter(c -> c == ';').count() == 1);
    }

    @Test
    void csvToSqlQuotedFieldAndDelimiter() {
        // 字段内含逗号且用双引号包裹
        String csv = "name,note\n\"张三, 先生\",\"age,28\"";
        String sql = Algorithms.csvToSql(csv, "t", ',', true, false, true);
        assertTrue(sql.contains("'张三, 先生'"));
        assertTrue(sql.contains("'age,28'"));
    }
}
