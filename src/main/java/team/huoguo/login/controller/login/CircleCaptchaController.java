package team.huoguo.login.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.huoguo.login.bean.CircleCaptcha;
import team.huoguo.login.bean.Result;
import team.huoguo.login.bean.ResultFactory;
import team.huoguo.login.utils.CircleCaptchaUtil;

import java.util.HashMap;

/**
 * @author GreenHatHG
 **/

@RestController
@RequestMapping(value="/api/v1")
public class CircleCaptchaController {

    private CircleCaptchaUtil circleCaptchaUtil;

    @Autowired
    public void setCircleCaptchaUtil(CircleCaptchaUtil circleCaptchaUtil) {
        this.circleCaptchaUtil = circleCaptchaUtil;
    }

    /**
     * 获取七牛上面验证码图片的文件名
     * @return
     */
    @GetMapping("/captcha")
    public Result getCaptcha()  {
        try{
            CircleCaptcha circleCaptcha = circleCaptchaUtil.getCircleCaptcha();
            HashMap<String, String> resp = new HashMap<>(2);
            resp.put("id", circleCaptcha.getId());
            resp.put("base64", circleCaptcha.getBase64Str());
            return ResultFactory.buildSuccessResult(resp);
        }catch (Exception e){
            e.printStackTrace();
            return ResultFactory.buildFailResult("获取验证码失败");
        }
    }
}
