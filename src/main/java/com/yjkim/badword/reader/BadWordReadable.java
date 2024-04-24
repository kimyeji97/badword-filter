package com.yjkim.badword.reader;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * bad-word reader interface
 */
public interface BadWordReadable
{
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BadWordReadable.class);

    /**
     * 비속어를 읽습니다.
     *
     * @return 읽은 비속어들
     */
    Set<String> read ();

    /**
     * 현재 reader의 이름 반환
     *
     * @return 이름
     */
    String getReaderName ();

    /**
     * reader 이름과 함께 로그를 남깁니다.
     *
     * @param message 로그 남길 메시지
     */
    default void recordLog (String... message)
    {
        log.info("[{}] {}", getReaderName(), StringUtils.join(message));
    }
}
