package com.yjkim.badword.business;

import com.yjkim.badword.config.BadWordProperties;
import com.yjkim.badword.exceptions.BadWordRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

/**
 * 욕설/비속어 필터링 서비스
 */
@Slf4j
@RequiredArgsConstructor
public class BadWordFilter
{
    private final BadWordProperties badWordProperties;

    /**
     * 마스킹 문자 세팅
     *
     * @param masking 마스킹 문자
     */
    public void setMaskingLetters (String masking)
    {
        badWordProperties.getMasking().setPattern(masking);
    }

    /**
     * 비속어 마스킹 처리
     *
     * @param text 문자
     * @return 비속어 마스킹 처리된 문자
     */
    public String masking (String text)
    {
        if (ObjectUtils.isEmpty(BadWordLoader.badWords))
        {
            return text;
        }

        String[] words = BadWordLoader.badWords.stream().filter(text::contains).toArray(String[]::new);
        for (String v : words)
        {
            String sub =badWordProperties.getMasking().getPattern().repeat(v.length());
            text = text.replace(v, sub);
        }
        return text;
    }

    /**
     * 비속어 포함 여부
     *
     * @param text 문자
     * @return true : 비속어 포함
     */
    public boolean isInclude (String text)
    {
        if (ObjectUtils.isEmpty(BadWordLoader.badWords))
        {
            return false;
        }

        return hasBadWordsRaw(text) || hasBadWordsRmBlank(text) || hasBadWordsRmSpecial(text);
    }

    /**
     * 비속어 미포함 여부
     *
     * @param text 문자
     * @return true : 비속어 없음
     */
    public boolean isNotInclude (String text)
    {
        return !isInclude(text);
    }

    /**
     * 비속어가 존재하면 에러 발생
     *
     * @param text 문자
     */
    public void assertNotInclude (String text)
    {
        if (isInclude(text))
        {
            throw new BadWordRequestException("비속어가 포함되어 있습니다.");
        }
    }

    /**
     * 비속어 완벽일치 하는게 존재하는지 체크
     *
     * @param text
     * @return
     */
    private boolean hasBadWordsRaw (String text)
    {
        if (text == null)
        {
            throw new BadWordRequestException("text is null.");
        }
        if (ObjectUtils.isEmpty(BadWordLoader.badWords))
        {
            return false;
        }
        return BadWordLoader.badWords.stream().anyMatch(text::contains);
    }

    /**
     * 공백 제거한 후 비속어 존재 여부 체크
     *
     * @param text
     * @return
     */
    private boolean hasBadWordsRmBlank (String text)
    {
        if (ObjectUtils.isEmpty(BadWordLoader.badWords))
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
    private boolean hasBadWordsRmSpecial (String text)
    {
        if (ObjectUtils.isEmpty(BadWordLoader.badWords))
        {
            return false;
        }
        return hasBadWordsRaw(text.replaceAll("[^0-9|a-z|^A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]", ""));
    }
}
