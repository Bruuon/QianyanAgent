package com.shanyangcode.infintechatagent.guardrail;

import com.github.houbb.sensitive.word.api.IWordAllow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomWordAllow implements IWordAllow {

    private static final String ALLOW_FILE_PATH = "SensitiveWord/sensitive-word-allow.txt";
    private List<String> allowWords;

    public CustomWordAllow() {
        this.allowWords = loadAllowWords();
    }

    private List<String> loadAllowWords() {
        List<String> words = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource(ALLOW_FILE_PATH);
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
                log.info("成功加载 {} 个白名单词汇", words.size());
            } else {
                log.warn("白名单文件不存在: {}", ALLOW_FILE_PATH);
            }
        } catch (IOException e) {
            log.error("读取白名单文件失败: {}", ALLOW_FILE_PATH, e);
        }
        return words;
    }

    @Override
    public List<String> allow() {
        return allowWords;
    }

}
