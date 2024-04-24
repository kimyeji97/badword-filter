package com.yjkim.badword.reader;

import com.yjkim.badword.exceptions.BadWordInternalServerException;
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
 * 텍스트 파일 Reader
 */
public class BadWordTxtFileReader implements BadWordReadable
{
    private static final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private final String[] paths;


    public BadWordTxtFileReader (String[] paths)
    {
        this.paths = paths;
    }

    @Override
    public String getReaderName ()
    {
        return "BadWord-TextFileReader";
    }

    /**
     * .txt 파일에서 읽어온 비속어
     *
     * @return 비속어
     */
    public Set<String> read ()
    {
        List<Resource> resources = this.findTxtFiles();
        if (resources.isEmpty())
        {
            this.recordLog("Not exist file.");
            return Collections.emptySet();
        }

        this.recordLog("Start Loading...");
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

                this.recordLog(String.valueOf(badWords.size()), " bad words loaded at ", resource.getFilename(), " file.");
            } catch (IOException e)
            {
                throw new BadWordInternalServerException(e);
            }
        }
        this.recordLog("End of loading...");

        return badWords;
    }

    /**
     * 텍스트 파일 찾기
     *
     * @return
     */
    private List<Resource> findTxtFiles ()
    {
        List<Resource> resources = new ArrayList<>();
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
