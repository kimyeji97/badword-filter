package com.yjkim.badword.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix = "bad-word")
public class BadWordProperties
{
    private Masking masking;
    private Storage storage;

    @Getter
    @Setter
    public static class Masking
    {
        private Boolean enable;
        private String pattern;
    }

    @Getter
    @Setter
    public static class Storage
    {
        private StorageSub txt;
        private StorageSub excel;
        private StorageSub db;
        private StorageSub text;
    }

    @Getter
    @Setter
    public static class StorageSub
    {
        private Boolean enable;
        private String[] paths;
        private String[] words;
    }
}
