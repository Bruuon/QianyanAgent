package com.shanyangcode.infintechatagent.ai;

import com.shanyangcode.infintechatagent.guardrail.SafeInputGuardrail;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
<<<<<<< HEAD
=======
import reactor.core.publisher.Flux;
>>>>>>> 7541c4b (add config)


@InputGuardrails({SafeInputGuardrail.class})
public interface AiChat {

    @SystemMessage(fromResource = "system-prompt/chat-bot.txt")
<<<<<<< HEAD
    String chat(@MemoryId String sessionId, @UserMessage String prompt);
=======
    String chat(@MemoryId Long sessionId, @UserMessage String prompt);


    @SystemMessage(fromResource = "system-prompt/chat-bot.txt")
    Flux<String> streamChat(@MemoryId Long sessionId, @UserMessage String prompt);
>>>>>>> 7541c4b (add config)
}
