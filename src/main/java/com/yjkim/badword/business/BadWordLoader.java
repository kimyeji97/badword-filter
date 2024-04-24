package com.yjkim.badword.business;

import com.yjkim.badword.config.BadWordProperties;
import com.yjkim.badword.reader.BadWordTextReader;
import com.yjkim.badword.reader.BadWordTxtFileReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class BadWordLoader implements InitializingBean
{
    private final BadWordProperties badWordProperties;

    public static final Set<String> badWords = new HashSet<>();

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

        return BadWordLoader.badWords.addAll(list);
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
            BadWordLoader.badWords.remove(s);
        }
        return true;
    }

    public void loading ()
    {
        log.info("===== Loading Bad-Word : START =====");
        badWords.clear();
        if (BooleanUtils.isTrue(badWordProperties.getStorage().getTxt().getEnable()))
        {
            BadWordTxtFileReader badWordTxtFileReader = new BadWordTxtFileReader(badWordProperties.getStorage().getTxt().getPaths());
            badWords.addAll(badWordTxtFileReader.read());
        }
        if (BooleanUtils.isTrue(badWordProperties.getStorage().getText().getEnable()))
        {
            BadWordTextReader badWordTextReader = new BadWordTextReader(badWordProperties.getStorage().getText().getWords());
            badWords.addAll(badWordTextReader.read());
        }
        log.info("===== Loading Bad-Word : END   =====");
    }

    @Override
    public void afterPropertiesSet ()
    {
        loading();
    }
}
