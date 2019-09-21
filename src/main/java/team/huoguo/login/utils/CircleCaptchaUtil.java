package team.huoguo.login.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.huoguo.login.service.QnUploadService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;

/**
 * 图像验证码工具
 * @author GreenHatHG
 **/

@Component
public class CircleCaptchaUtil {

    private QnUploadService qnUploadService;
    private RedisUtil redisUtil;

    /**
     * 定义图形验证码的长、宽、验证码字符数、干扰线宽度
     */
    private ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(250, 100, 4, 4);
    private String path = null;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Autowired
    public void setQnUploadService(QnUploadService qnUploadService) {
        this.qnUploadService = qnUploadService;
    }

    public String getPath() {
        return path;
    }

    /**
     * 获取保存图片的路径，保存在项目src/main/resources/img
     */
    public CircleCaptchaUtil(){
        String classesStr =  Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        Path classes = Paths.get(classesStr);
        path = classes.getParent().getParent().toString();
        path += "/src/main/resources/img/";
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
        hashMap.put("code", code);
        return hashMap;
    }

    public HashMap<String, String> getAndUpload(){
        try{
            HashMap<String, String> hashMap = setCircleCaptcha();
            String fileName = hashMap.get("fileName");
            qnUploadService.uploadFile(hashMap.get("file"), fileName);
            redisUtil.setString(fileName, hashMap.get("code").toLowerCase());
            redisUtil.expire(fileName, 10*60);
            HashMap<String, String> resp = new HashMap<>(1);
            resp.put("fileName", fileName);
            return resp;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
