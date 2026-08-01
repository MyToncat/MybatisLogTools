package com.devtoolbox.tools;

import com.devtoolbox.core.Algorithms;
import com.devtoolbox.core.ToolPanel;
import com.devtoolbox.core.ToolShell;
import net.miginfocom.swing.MigLayout;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * JSON 格式化 / 校验 / 压缩工具。
 */
public class JsonTool extends ToolPanel {

    private RSyntaxTextArea input;
    private RSyntaxTextArea output;

    public JsonTool() {
        super("json", "JSON 解析", "开发工具", "支持 JSON 美化（格式化）、压缩（去空白）与合法性校验，数据仅在本地处理。");
    }

    @Override
    protected void build(ToolShell shell) {
        input = new RSyntaxTextArea();
        input.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        input.setCodeFoldingEnabled(true);
        input.setTabSize(2);
        RTextScrollPane inScroll = new RTextScrollPane(input);

        output = new RSyntaxTextArea();
        output.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        output.setEditable(false);
        RTextScrollPane outScroll = new RTextScrollPane(output);

        shell.addContent(new JLabel("输入"), "growx");
        shell.addContent(inScroll, "grow, hmin 120");
        shell.addContent(new JLabel("输出"), "growx, gaptop 8");
        shell.addContent(outScroll, "grow, hmin 120");

        JButton formatBtn = new JButton("格式化");
        JButton minifyBtn = new JButton("压缩");
        JButton validateBtn = new JButton("校验");
        JButton clearBtn = new JButton("清空");
        ToolShell.styleButton(clearBtn, false);

        formatBtn.addActionListener(e -> transform(true, shell));
        minifyBtn.addActionListener(e -> transform(false, shell));
        validateBtn.addActionListener(e -> doValidate(shell));
        clearBtn.addActionListener(e -> {
            input.setText("");
            output.setText("");
            shell.setStatus("已清空", ToolShell.StatusType.INFO);
        });

        shell.addHeaderButton(formatBtn);
        shell.addHeaderButton(minifyBtn);
        shell.addHeaderButton(validateBtn);
        shell.addHeaderButton(clearBtn);
    }

    @Override
    public void seedDemo() {
        input.setText("{\"name\":\"张三\",\"age\":28,\"hobbies\":[\"reading\",\"coding\"],\"address\":{\"city\":\"北京\",\"zip\":100000}}");
        try {
            output.setText(Algorithms.jsonFormat(input.getText()));
        } catch (Exception ignore) {
        }
    }

    private void transform(boolean pretty, ToolShell shell) {
        String text = input.getText().trim();
        if (text.isEmpty()) {
            shell.setStatus("请输入 JSON 内容", ToolShell.StatusType.INFO);
            return;
        }
        try {
            String result = pretty ? Algorithms.jsonFormat(text) : Algorithms.jsonMinify(text);
            output.setText(result);
            shell.setStatus(pretty ? "已格式化" : "已压缩", ToolShell.StatusType.SUCCESS);
        } catch (Exception ex) {
            output.setText("");
            shell.setStatus("JSON 解析失败：" + ex.getMessage(), ToolShell.StatusType.ERROR);
        }
    }

    private void doValidate(ToolShell shell) {
        String text = input.getText().trim();
        if (text.isEmpty()) {
            shell.setStatus("请输入 JSON 内容", ToolShell.StatusType.INFO);
            return;
        }
        if (Algorithms.jsonValid(text)) {
            output.setText("");
            shell.setStatus("✓ 合法 JSON", ToolShell.StatusType.SUCCESS);
        } else {
            output.setText("");
            shell.setStatus("✗ 非法 JSON", ToolShell.StatusType.ERROR);
        }
    }
}
