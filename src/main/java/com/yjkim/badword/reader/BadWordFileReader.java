package com.yjkim.badword.reader;

import com.yjkim.badword.exceptions.BadWordInternalServerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 비속어 파일 Reader
 */
@Slf4j
public class BadWordFileReader
{
    private String[] paths;

    private static String path = "classpath*:templates/bad-words.txt";
    private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    public BadWordFileReader (String[] paths)
    {
        this.paths = paths;
    }

    public Set<String> readTxtFiles ()
    {
        List<Resource> resources = this.findTxtFiles();
        if (resources.isEmpty())
        {
            log.info("[BadWord] Not exist file.");
            return Collections.emptySet();
        }

        log.info("[BadWord] Start Loading...");

        Set<String> badWords = new HashSet<>();
        for (Resource resource : resources)
        {
            try (InputStream is = resource.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is)))
            {

                String line;
                while ((line = reader.readLine()) != null)
                {
                    if (StringUtils.isBlank(line))
                    {
                        continue;
                    }
                    badWords.add(line.replace(" ", ""));
                }

                log.info("[BadWord] {} bad words loaded at {} file.", badWords.size(), resource.getFilename());
            } catch (IOException e)
            {
                throw new BadWordInternalServerException(e);
            }
        }

        log.info("[BadWord] End of loading...");

        return badWords;
    }

    /**
     * 텍스트 파일 찾기
     *
     * @return
     */
    private List<Resource> findTxtFiles ()
    {
        List<Resource> resources;
        try
        {
            resources = new ArrayList<>(Arrays.stream(resourcePatternResolver.getResources(path)).toList());
        } catch (IOException ex)
        {
            throw new BadWordInternalServerException(ex);
        }

        if (ObjectUtils.isNotEmpty(paths))
        {
            for (String path : paths)
            {
                try
                {
                    resources.addAll(Arrays.stream(resourcePatternResolver.getResources(path)).toList());
                } catch (IOException ex)
                {
                    throw new BadWordInternalServerException(ex);
                }
            }
        }

        return resources;
    }

}
