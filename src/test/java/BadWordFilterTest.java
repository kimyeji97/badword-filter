import com.yjkim.badword.business.BadWordFilter;
import com.yjkim.badword.business.BadWordLoader;
import com.yjkim.badword.config.BadWordFilterConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {BadWordFilterConfig.class})
public class BadWordFilterTest
{

    @Autowired
    private BadWordFilter badWordFilter;

    @DisplayName("lib loading")
    @Test
    public void libLoading ()
    {
        Assertions.assertFalse(badWordFilter.isInclude("안녕하세요"));
        Assertions.assertTrue(badWordFilter.isInclude("ㅅㅂ"));

        System.out.println(badWordFilter.masking("안녕하세요? ㅅㅂ는 나쁜말 이에요.."));
        System.out.println(badWordFilter.masking("안녕하세요? 젠장은 나쁜말 이에요.."));
        System.out.println(badWordFilter.masking("안녕하세요? 똥 방구나 먹어라.."));

        System.out.println("\r\n===== 마스킹 문자 바꿔볼게요~ =====");
        badWordFilter.setMaskingLetters("?");
        System.out.println(badWordFilter.masking("안녕하세요? ㅅㅂ는 나쁜말 이에요.."));
    }
}
