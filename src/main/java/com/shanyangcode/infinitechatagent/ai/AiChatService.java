package com.shanyangcode.infinitechatagent.ai;

<<<<<<< HEAD
=======
import com.shanyangcode.infinitechatagent.config.McpToolConfig;
>>>>>>> 7541c4b (add config)
import com.shanyangcode.infinitechatagent.tool.EmailTool;
import com.shanyangcode.infinitechatagent.tool.RagTool;
import com.shanyangcode.infinitechatagent.tool.TimeTool;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
<<<<<<< HEAD
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
=======
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
>>>>>>> 7541c4b (add config)
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

<<<<<<< HEAD
=======
import java.util.List;

>>>>>>> 7541c4b (add config)
@Configuration
public class AiChatService {

    @Resource
    private ChatModel chatModel;

    @Resource
    private McpToolProvider mcpToolProvider;

    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    @Resource
    private ContentRetriever contentRetriever;

    @Resource
    private RagTool ragTool;

    @Resource
    private EmailTool emailTool;

<<<<<<< HEAD
=======
    @Resource
    private StreamingChatModel streamingChatModel;

>>>>>>> 7541c4b (add config)
    @Bean
    public AiChat aiChat() {

        return AiServices.builder(AiChat.class)
                .chatModel(chatModel)
<<<<<<< HEAD
=======
                .streamingChatModel(streamingChatModel)
>>>>>>> 7541c4b (add config)
                .contentRetriever(contentRetriever)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory
                        .builder()
                        .id(memoryId)
                        .chatMemoryStore(redisChatMemoryStore)
                        .maxMessages(20)
                        .build())
                .tools(new TimeTool(), ragTool, emailTool)
                .toolProvider(mcpToolProvider)
                .build();
    }

}