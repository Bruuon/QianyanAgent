package com.shanyangcode.infintechatagent.config;

import com.shanyangcode.infintechatagent.memory.TokenCountChatMemoryCompressor;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Configuration
@ConfigurationProperties(prefix = "chat.memory")
@Data
@Slf4j
public class ChatMemoryConfig {

    private int maxMessages = 20;
    private Compression compression = new Compression();
    private Redis redis = new Redis();

    @PostConstruct
    public void init() {
        log.info("=== 会话记忆压缩配置已加载 ===");
        log.info("最大消息数: {}", maxMessages);
        log.info("Token阈值: {}", compression.getTokenThreshold());
        log.info("保留最近轮数: {}", compression.getRecentRounds());
        log.info("最近对话Token上限: {}", compression.getRecentTokenLimit());
        log.info("降级保留轮数: {}", compression.getFallbackRecentRounds());
        log.info("分布式锁过期时间: {}秒", redis.getLock().getExpireSeconds());
        log.info("分布式锁重试次数: {}", redis.getLock().getRetryTimes());
        log.info("==============================");
    }

    @Data
    public static class Compression {
        private int tokenThreshold = 6000;
        private int recentRounds = 5;
        private int recentTokenLimit = 2000;
        private int summaryTokenLimit = 500;
        private String summaryPrompt;
        private int fallbackRecentRounds = 10;
    }

    @Data
    public static class Redis {
        private int ttlSeconds = 3600;
        private Lock lock = new Lock();
    }

    @Data
    public static class Lock {
        private int expireSeconds = 5;
        private int retryTimes = 3;
        private int retryIntervalMs = 100;
    }

    @Bean
    public TokenCountChatMemoryCompressor tokenCountChatMemoryCompressor(HuggingFaceTokenizer qwenTokenizer) {
        log.info("创建TokenCountChatMemoryCompressor Bean");
        return new TokenCountChatMemoryCompressor(
            compression.getRecentRounds(),
            compression.getRecentTokenLimit(),
            qwenTokenizer
        );
    }

    /**
     * 初始化企业级本地分词器
     */
    @Bean
    public HuggingFaceTokenizer qwenTokenizer() throws Exception {
        log.info("正在加载 Qwen 本地词表...");

        // 注意：Spring Boot 打包成 jar 后，无法直接通过路径访问文件。
        // 标准做法是将 resource 里的文件流复制到临时目录再加载
        ClassPathResource resource = new ClassPathResource("qwen/tokenizer.json");
        Path tempFile = Files.createTempFile("qwen_tokenizer", ".json");

        try (InputStream is = resource.getInputStream()) {
            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        // 构建并返回 Tokenizer
        HuggingFaceTokenizer tokenizer = HuggingFaceTokenizer.newInstance(tempFile);

        // 记得删除临时文件
        tempFile.toFile().deleteOnExit();

        log.info("Qwen 本地词表加载完成！");
        return tokenizer;
    }
}
