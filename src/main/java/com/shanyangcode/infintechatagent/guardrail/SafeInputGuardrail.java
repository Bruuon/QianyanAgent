package com.shanyangcode.infintechatagent.guardrail;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;

import java.util.Set;

public class SafeInputGuardrail implements InputGuardrail {




    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String inputText = userMessage.singleText();



            if (SensitiveWordHelper.contains(inputText)) {
                return fatal("提问不能包含敏感词！！！！！");
            }
        

        return success();
    }
}
