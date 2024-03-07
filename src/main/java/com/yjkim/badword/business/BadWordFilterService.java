package com.yjkim.badword.business;

import com.yjkim.badword.exceptions.BadWordRequestException;
import com.yjkim.badword.reader.BadWordFileReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 욕설/비속어 필터링 서비스
 */
@Slf4j
public class BadWordFilterService implements InitializingBean
{
    @Value("${badword.masking:*}")
    public String masking;
    @Value("${badword.paths.txt}")
    public String[] paths;

    private static Set<String> badWords;
    private static BadWordFileReader badWordFileReader;

    /**
     * 마스킹 문자 세팅
     *
     * @param masking 마스킹 문자
     */
    public void setMaskingLetters (String masking)
    {
        this.masking = masking;
    }

    /**
     * 단어 추가
     *
     * @param words 단어
     * @return 추가여부
     */
    public boolean addWords (String words)
    {
        return this.addWords(Collections.singletonList(words));
    }

    /**
     * 단어 일괄 추가
     *
     * @param list 추가할 단어들
     * @return 추가여부
     */
    public boolean addWords (List<String> list)
    {
        if (ObjectUtils.isEmpty(list))
        {
            return false;
        }

        return badWords.addAll(list);
    }

    /**
     * 단어 삭제
     *
     * @param words 단어
     * @return 삭제여부
     */
    public boolean removeWords (String words)
    {
        return this.removeWords(Collections.singletonList(words));
    }

    /**
     * 단어 일괄 삭제
     *
     * @param list 삭제할 단어들
     * @return 삭제여부
     */
    public boolean removeWords (List<String> list)
    {
        if (ObjectUtils.isEmpty(list))
        {
            return false;
        }

        for (String s : list)
        {
            badWords.remove(s);
        }
        return true;
    }

    /**
     * 비속어 마스킹 처리
     *
     * @param text 문자
     * @return 비속어 마스킹 처리된 문자
     */
    public String masking (String text)
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
     * 비속어 포함 여부
     *
     * @param text 문자
     * @return true : 비속어 포함
     */
    public boolean isInclude (String text)
    {
        if (ObjectUtils.isEmpty(badWords))
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
    private boolean hasBadWordsRmBlank (String text)
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
    private boolean hasBadWordsRmSpecial (String text)
    {
        if (ObjectUtils.isEmpty(badWords))
        {
            return false;
        }
        return hasBadWordsRaw(text.replaceAll("[^0-9|a-z|^A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]", ""));
    }

    @Override
    public void afterPropertiesSet ()
    {
        log.info("===== Init BadWordFilterUtils : START =====");
        badWordFileReader = new BadWordFileReader(paths);
        badWords = badWordFileReader.readTxtFiles();
        log.info("===== Init BadWordFilterUtils : END   =====");
    }
}
