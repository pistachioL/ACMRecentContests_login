package team.huoguo.login.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Objects;

/**
 * 图像验证码工具
 * @author GreenHatHG
 **/

@Component
public class CircleCaptchaUtil {

    /**
     * 定义图形验证码的长、宽、验证码字符数、干扰线宽度
     */

    private ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
    private String path = null;
    private RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public HashMap<String, String> setCircleCaptcha(){
        HashMap<String, String> hashMap = new HashMap<>(2);
        //重新生成验证码
        captcha.createCode();
        //生成不带-的UUID字符串不再需要做字符替换，性能提升一倍左右
        String fileName = IdUtil.simpleUUID();
        String filePath = path + fileName + ".png";
        captcha.write(filePath);
        String code = captcha.getCode();
        redisUtil.setString(fileName, code);
        hashMap.put("fileName", fileName);
        hashMap.put("file", filePath);
        return hashMap;
    }

    /**
     * 获取保存图片的路径，保存在项目src/main/resources/img
     */
    public CircleCaptchaUtil(){
        String classes =  Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        path = classes.substring(0, classes.length() - 15);
        path += "src/main/resources/img/";
    }
}
