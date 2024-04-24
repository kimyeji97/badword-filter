package com.yjkim.badword.reader;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * property 설정된 텍스트 Reader
 */
public class BadWordTextReader implements BadWordReadable
{

    private final String[] words;

    public BadWordTextReader (String[] words)
    {
        this.words = words;
    }

    @Override
    public Set<String> read ()
    {
        if (ObjectUtils.isEmpty(words))
        {
            this.recordLog("Empty words.");
            return Collections.emptySet();
        }

        Set<String> collect = Arrays.stream(this.words).collect(Collectors.toSet());
        this.recordLog(String.valueOf(collect.size()), " bad words loaded.");

        return collect;
    }

    @Override
    public String getReaderName ()
    {
        return "BadWord-TextReader";
    }
}
