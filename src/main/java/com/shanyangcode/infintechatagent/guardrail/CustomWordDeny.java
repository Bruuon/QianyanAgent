package com.shanyangcode.infintechatagent.guardrail;

import com.github.houbb.sensitive.word.api.IWordDeny;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomWordDeny implements IWordDeny {

    private static final String DENY_FILE_PATH = "SensitiveWord/sensitive-word-deny.txt";
    private List<String> denyWords;

    public CustomWordDeny() {
        this.denyWords = loadDenyWords();
    }

    private List<String> loadDenyWords() {
        List<String> words = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource(DENY_FILE_PATH);
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (!line.isEmpty() && !line.startsWith("#")) {
                            words.add(line);
                        }
                    }
                }
                log.info("成功加载 {} 个黑名单词汇", words.size());
            } else {
                log.warn("黑名单文件不存在: {}", DENY_FILE_PATH);
            }
        } catch (IOException e) {
            log.error("读取黑名单文件失败: {}", DENY_FILE_PATH, e);
        }
        return words;
    }

    @Override
    public List<String> deny() {
        return denyWords;
    }

}
