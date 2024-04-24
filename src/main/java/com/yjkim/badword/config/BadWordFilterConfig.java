package com.yjkim.badword.config;

import com.yjkim.badword.business.BadWordFilter;
import com.yjkim.badword.business.BadWordLoader;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

/**
 * Spring boot auto configuration
 */
@AutoConfiguration
@ConfigurationPropertiesScan
public class BadWordFilterConfig
{
    /**
     * 비속어 필터링 서비스
     *
     * @return 비속어 필터링 서비스 Bean
     */
    @Bean
    public BadWordFilter badWordFilter (BadWordProperties badWordProperties)
    {
        return new BadWordFilter(badWordProperties);
    }

    /**
     * 비속어 메모리 Loading 서비스
     *
     * @return
     */
    @Bean
    public BadWordLoader badWordLoader (BadWordProperties badWordProperties)
    {
        return new BadWordLoader(badWordProperties);
    }
}
