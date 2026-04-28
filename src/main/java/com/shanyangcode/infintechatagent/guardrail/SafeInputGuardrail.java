package com.shanyangcode.infintechatagent.guardrail;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SafeInputGuardrail implements InputGuardrail, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        SensitiveWordBs sensitiveWordBs = applicationContext.getBean(SensitiveWordBs.class);
        String inputText = userMessage.singleText();

        if (sensitiveWordBs.contains(inputText)) {
            var foundWords = sensitiveWordBs.findAll(inputText);
            log.warn("敏感词拦截 - 原文: {}, 命中词: {}", inputText, foundWords);
            return fatal("提问不能包含敏感词：" + foundWords);
        }

        return success();
    }
}
