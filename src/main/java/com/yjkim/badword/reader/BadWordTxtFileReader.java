package com.yjkim.badword.reader;

import com.yjkim.badword.exceptions.BadWordInternalServerException;
import com.yjkim.badword.utils.FileUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;
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
        Set<String> badWords = new HashSet<>();

        /*
         * Resource 파일 찾고 읽기
         */
        this.recordLog("Start loading (resource)...");
        List<Resource> resources = this.findAtResource();
        if (!resources.isEmpty())
        {
            for (Resource resource : resources)
            {
                // Line 읽어서 badWords에 세팅
                try
                {
                    this.readByLine(badWords, resource.getFile());
                } catch (IOException e)
                {
                    this.recordLog("Maybe '", resource.getFilename(), "' is not resource. => ", e.getMessage());
                }
            }
        }
        this.recordLog("End of loading (resource)...");

        /*
         * Local 파일 찾고 읽기
         */
        this.recordLog("Start loading (local)...");
        for (String path : paths)
        {
            // .txt 확장자 체크
            File file = FileUtil.getExistingFile(path);
            if (!FileUtil.getExtension(path).equals(".txt"))
            {
                this.recordLog("'", path, "' is not .txt file.");
            }
            // Line 읽어서 badWords에 세팅
            try
            {
                this.readByLine(badWords, file);
            } catch (IOException ex)
            {
                this.recordLog("Maybe '", file.getAbsolutePath(), "' is not File. => ", ex.getMessage());
            }
        }
        this.recordLog("End of loading (local)...");

        return badWords;
    }

    /**
     * 해당 파일을 라인 단위로 읽고 비속어 리스트에 세팅
     *
     * @param badWords 비속어 리스트
     * @param file     파일
     * @throws IOException 파일 IO 에러
     */
    private void readByLine (Set<String> badWords, File file) throws IOException
    {
        if (file != null && file.exists() && file.isFile())
        {
            int bSize = badWords.size();
            FileUtil.readAndProcessByLine(file, (line) -> badWords.add(line.replace(" ", "")));
            this.recordLog(String.valueOf(badWords.size() - bSize), " bad words loaded at ", file.getAbsolutePath(), " file.");
        }
    }

    /**
     * 리소스 텍스트 파일 찾기
     *
     * @return
     */
    private List<Resource> findAtResource ()
    {
        if (ObjectUtils.isEmpty(paths))
        {
            return Collections.emptyList();
        }

        List<Resource> resources = new ArrayList<>();
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

        return resources;
    }

}
