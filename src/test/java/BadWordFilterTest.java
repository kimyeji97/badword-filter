import com.yjkim.badword.business.BadWordFilterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = BadWordFilterService.class)
public class BadWordFilterTest
{

    @Autowired
    private BadWordFilterService badWordFilterService;


    @DisplayName("lib loading")
    @Test
    public void libLoading()
    {
        Assertions.assertFalse(badWordFilterService.isInclude("안녕하세요"));
        Assertions.assertTrue(badWordFilterService.isInclude("ㅅㅂ"));

        System.out.println(badWordFilterService.masking("안녕하세요? ㅅㅂ는 나쁜말 이에요.."));
        badWordFilterService.setMaskingLetters("?");
        System.out.println(badWordFilterService.masking("안녕하세요? ㅅㅂ는 나쁜말 이에요.."));

        System.out.println(badWordFilterService.masking("안녕하세요? 젠장은 나쁜말 이에요.."));
    }
}
