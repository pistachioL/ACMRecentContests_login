package team.huoguo.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team.huoguo.login.bean.Result;
import team.huoguo.login.service.ResultFactory;
import team.huoguo.login.utils.CircleCaptchaUtil;

import java.util.HashMap;

/**
 * @author GreenHatHG
 **/

@RestController
public class CircleCaptchaService {

    private CircleCaptchaUtil circleCaptchaUtil;

    @Autowired
    public void setCircleCaptchaUtil(CircleCaptchaUtil circleCaptchaUtil) {
        this.circleCaptchaUtil = circleCaptchaUtil;
    }

    /**
     * 获取七牛上面验证码图片的文件名
     * @return
     */
    @GetMapping("/v1/captcha")
    public Result getCaptcha()  {
        HashMap<String, String> resp = circleCaptchaUtil.getAndUpload();
        if(resp == null){
            return ResultFactory.buildFailResult("获取验证码失败");
        }else{
            return ResultFactory.buildSuccessResult(resp);
        }
    }
}
