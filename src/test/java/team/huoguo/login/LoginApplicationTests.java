package team.huoguo.login;

import cn.hutool.core.date.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginApplicationTests {

    @Test
    public void test(){
        System.out.println(DateUtil.tomorrow().toString().substring(0, 10));
    }

}
