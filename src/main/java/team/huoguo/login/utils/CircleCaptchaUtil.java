package team.huoguo.login.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.huoguo.login.bean.CircleCaptcha;

/**
 * 图像验证码工具
 * @author GreenHatHG
 **/

@Component
public class CircleCaptchaUtil{

    private RedisUtil redisUtil;
    /**
     * 定义图形验证码的长、宽
     */
    private LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100);
    private CircleCaptcha circleCaptcha = new CircleCaptcha();

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public CircleCaptcha getCircleCaptcha(){
        //重新生成验证码
        captcha.createCode();
        //生成不带-的UUID字符串不再需要做字符替换，性能提升一倍左右
        circleCaptcha.setId(IdUtil.simpleUUID());
        circleCaptcha.setCode(captcha.getCode());
        circleCaptcha.setBase64Str(captcha.getImageBase64());
        redisUtil.setString(circleCaptcha.getId(), circleCaptcha.getCode());
        redisUtil.expire(circleCaptcha.getId(), 10*60);
        return circleCaptcha;
    }
}
