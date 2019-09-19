package team.huoguo.login;

import com.qiniu.common.QiniuException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.huoguo.login.service.QnUploadService;
import team.huoguo.login.utils.CircleCaptchaUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginApplicationTests {

    @Autowired
    CircleCaptchaUtil circleCaptchaUtil;

    @Autowired
    QnUploadService qnUploadService;

    @Test
    public void test() throws QiniuException {
        System.out.println(circleCaptchaUtil.getPath());
    }
}
