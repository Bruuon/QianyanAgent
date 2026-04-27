package com.shanyangcode.infintechatagent.memory;

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TokenCountChatMemoryCompressor {

    private final int recentRounds;
    private final int recentTokenLimit;
    private final HuggingFaceTokenizer tokenizer;

    public TokenCountChatMemoryCompressor(int recentRounds, int recentTokenLimit, HuggingFaceTokenizer tokenizer) {
        this.recentRounds = recentRounds;
        this.recentTokenLimit = recentTokenLimit;
        this.tokenizer = tokenizer;
    }

    public List<ChatMessage> compress(List<ChatMessage> messages) {
        if (messages.size() <= recentRounds * 2) {
            log.debug("消息数{}未超过阈值{}，无需压缩", messages.size(), recentRounds * 2);
            return messages;
        }

        int splitIndex = messages.size() - recentRounds * 2;
        List<ChatMessage> oldMessages = messages.subList(0, splitIndex);
        List<ChatMessage> recentMessages = messages.subList(splitIndex, messages.size());

        int recentTokens = estimateTokens(recentMessages);
        if (recentTokens > recentTokenLimit) {
            log.warn("⚠️ 最近消息Token数{}超过上限{}", recentTokens, recentTokenLimit);
        }

        log.info("📊 压缩统计 - 历史消息:{}条, 保留最近:{}条", oldMessages.size(), recentMessages.size());
        String summary = generateSummary(oldMessages);

        List<ChatMessage> compressed = new ArrayList<>();
        compressed.add(SystemMessage.from("历史对话摘要: " + summary));
        compressed.addAll(recentMessages);

        log.info("✅ 压缩完成 - 原{}条 → 新{}条(摘要1条+最近{}条)",
            messages.size(), compressed.size(), recentMessages.size());
        return compressed;
    }

    private String generateSummary(List<ChatMessage> messages) {
        log.debug("生成摘要 - 处理{}条历史消息", messages.size());
        StringBuilder summary = new StringBuilder();
        summary.append("共").append(messages.size()).append("轮对话。");

        for (int i = 0; i < Math.min(3, messages.size()); i++) {
            ChatMessage msg = messages.get(i);
            String text = extractText(msg);
            if (text != null && !text.isEmpty()) {
                summary.append(" ").append(text.substring(0, Math.min(50, text.length())));
            }
        }

        log.debug("摘要生成完成 - 长度:{}字符", summary.length());
        return summary.toString();
    }

    private String extractText(ChatMessage msg) {
        if (msg instanceof AiMessage aiMsg) {
            return aiMsg.text();
        } else if (msg instanceof UserMessage userMsg) {
            return userMsg.singleText();
        } else if (msg instanceof SystemMessage sysMsg) {
            return sysMsg.text();
        }
        return "";
    }

    public int estimateTokens(List<ChatMessage> messages) {
        int total = 0;
        for (ChatMessage msg : messages) {
            String text = extractText(msg);
            if (text != null && !text.isEmpty()) {
                int tokens = tokenizer.encode(text).getTokens().length;
                total += tokens;
            }
        }
        return total;
    }
}
