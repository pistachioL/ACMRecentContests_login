package team.huoguo.login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.huoguo.login.entity.remind.QuartzJobService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginApplicationTests {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private QuartzJobService quartzJobService;

    @Test
    public void test() throws SchedulerException {
        System.out.println(quartzJobService.selectAllByJobGroup("ff8080816e0778e3016e078e62950000"));
    }
}
