package com.shanyangcode.infintechatagent.config;

import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import com.shanyangcode.infintechatagent.guardrail.CustomWordAllow;
import com.shanyangcode.infintechatagent.guardrail.CustomWordDeny;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SensitiveWordConfig {

    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        IWordAllow wordAllow = WordAllows.chains(
                WordAllows.defaults(),
                new CustomWordAllow()
        );

        IWordDeny wordDeny = WordDenys.chains(
                WordDenys.defaults(),
                new CustomWordDeny()
        );

        return SensitiveWordBs.newInstance()
                .wordAllow(wordAllow)
                .wordDeny(wordDeny)
                .ignoreCase(true)
                .ignoreWidth(true)
                .ignoreNumStyle(true)
                .ignoreChineseStyle(true)
                .ignoreEnglishStyle(true)
                .ignoreRepeat(false)
                .enableNumCheck(false)
                .enableEmailCheck(false)
                .enableUrlCheck(false)
                .init();
    }
}
