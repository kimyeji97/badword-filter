package com.yjkim.badword.config;

import com.yjkim.badword.business.BadWordFilterService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Spring boot auto configuration
 */
@AutoConfiguration
public class BadWordFilterConfig
{
    /**
     * 비속어 필터링 서비스
     *
     * @return 비속어 필터링 서비스 Bean
     */
    @Bean
    public BadWordFilterService badWordFilterService ()
    {
        return new BadWordFilterService();
    }
}
