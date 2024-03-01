package com.yjkim.badword.business;

import com.yjkim.badword.framework.util.ResourceScanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 욕설/비속어 필터링 클래스
 */
@Slf4j
@Service
public class BadWordFilterService implements InitializingBean
{
    private Set<String> badWords;
    private String masking;

    public BadWordFilterService()
    {
        this("*");
    }

    public BadWordFilterService(String masking)
    {
        this.masking = masking;
    }

    /**
     * 비속어 마스킹 처리
     *
     * @param text
     * @return
     */
    public String masking(String text)
    {
        if (ObjectUtils.isEmpty(badWords))
        {
            return text;
        }

        String[] words = badWords.stream().filter(text::contains).toArray(String[]::new);
        for (String v : words)
        {
            String sub = masking.repeat(v.length());
            text = text.replace(v, sub);
        }
        return text;
    }

    /**
     * 비속어 존재 여부
     *
     * @param text
     * @return
     */
    public boolean check(String text)
    {
        if (ObjectUtils.isEmpty(badWords))
        {
            return false;
        }

        return hasBadWordsRaw(text) || hasBadWordsRmBlank(text) || hasBadWordsRmSpecial(text);
    }

    /**
     * 비속어가 존재하면 exception
     *
     * @param text
     */
    public void assertNoBadWords(String text)
    {
        if (StringUtils.isNotBlank(text) && check(text))
        {
            throw new RuntimeException("욕설 및 비속어가 포함되어 있습니다.");
        }
    }

    /**
     * 비속어 완벽일치 하는게 존재하는지 체크
     *
     * @param text
     * @return
     */
    private boolean hasBadWordsRaw(String text)
    {
        if (ObjectUtils.isEmpty(badWords))
        {
            return false;
        }
        return badWords.stream().anyMatch(text::contains);
    }

    /**
     * 공백 제거한 후 비속어 존재 여부 체크
     *
     * @param text
     * @return
     */
    private boolean hasBadWordsRmBlank(String text)
    {
        if (ObjectUtils.isEmpty(badWords))
        {
            return false;
        }
        return hasBadWordsRaw(text.replace(" ", ""));
    }

    /**
     * 특수문자 제거한 후 비속어 존재 여부 체크
     *
     * @param text
     * @return
     */
    private boolean hasBadWordsRmSpecial(String text)
    {
        if (ObjectUtils.isEmpty(badWords))
        {
            return false;
        }
        return hasBadWordsRaw(text.replaceAll("[^0-9|a-z|^A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]", ""));
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        badWords = new HashSet<>();

        // TODO YJ 2023.06.23 디비로 넣을 예정
        Resource[] resources = ResourceScanner.findResources("classpath*:/templates/badwords/bad-words.txt");
        if (0 == resources.length)
        {
            log.info("[BadWord] Not exist file.");
            return;
        }

        Resource resource = resources[0];
        try (InputStream is = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is)))
        {
            log.info("[BadWord] Loading bad words...");

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                if (StringUtils.isBlank(line))
                {
                    return;
                }
                badWords.add(line.replace(" ", ""));
            }

            log.info("[BadWord] {} bad words loaded.", badWords.size());
        } catch (IOException e)
        {
            log.error(e.getMessage(), e);
        }
    }
}
