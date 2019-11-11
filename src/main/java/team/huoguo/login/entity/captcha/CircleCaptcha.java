package team.huoguo.login.entity.captcha;

import lombok.Data;

/**
 * 验证码类
 * @author GreenHatHG
 **/

@Data
public class CircleCaptcha {

    /**
     * 验证码内容
     */
    private String code;
    /**
     * 区别验证码的id
     */
    private String id;
    private String base64Str;
}
