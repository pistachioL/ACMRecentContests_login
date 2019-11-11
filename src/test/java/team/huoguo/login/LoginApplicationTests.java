package team.huoguo.login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.huoguo.login.common.utils.RedisUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void test() throws SchedulerException {


    }
}
