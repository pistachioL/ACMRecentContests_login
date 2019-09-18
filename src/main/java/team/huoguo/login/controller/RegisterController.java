package team.huoguo.login.controller;

import cn.hutool.core.lang.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.huoguo.login.bean.Result;
import team.huoguo.login.bean.UserInfo;
import team.huoguo.login.service.MailService;
import team.huoguo.login.service.ResultFactory;
import team.huoguo.login.service.UserRepository;
import team.huoguo.login.utils.Argon2Util;
import team.huoguo.login.utils.RedisUtil;

import java.util.Map;

/**
 * @author GreenHatHG
 */

@RestController
public class RegisterController {

    private UserRepository userRepository;
    private RedisUtil redisUtil;
    private MailService mailService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/v1/register")
    public Result register(@RequestBody UserInfo payload, @RequestParam(name = "code") String code) {
        String username = payload.getUsername();
        String redisCode = redisUtil.getString(username).toString();
        if(redisCode == null || !redisCode.equals(code)){
            return ResultFactory.buildFailResult("验证码已过期");
        }

        String password = payload.getPassword();
        String mail = payload.getMail();
        if(!Validator.isEmail(mail)){
            return ResultFactory.buildFailResult("邮箱格式不对");
        }
        if(userRepository.findByMail(mail) != null){
            return ResultFactory.buildFailResult("邮箱已存在");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(Argon2Util.hash(password));
        userInfo.setMail(mail);

        try {
            userRepository.save(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultFactory.buildFailResult("账号已存在");
        }
        return ResultFactory.buildSuccessResult("成功");
    }

    @PostMapping("/v1/code")
    public Result getCode(@RequestBody Map<String, String> payload){
        String username = payload.get("username");
        String mail = payload.get("mail");
        if(!Validator.isEmail(mail)){
            return ResultFactory.buildFailResult("邮箱格式不对");
        }
        if(userRepository.findByMail(mail) != null){
            return ResultFactory.buildFailResult("邮箱已存在");
        }
        if(redisUtil.tooManyTimes(mail)){
            return ResultFactory.buildFailResult("该邮箱发送次数，请稍后再试");
        }
        String code = mailService.getCode();
        System.out.println("code:  "+code);
        //mailService.sendMail(mail, code);
        //重复申请的话，会重新生成code,十分钟内最多三次
        redisUtil.setString(username, code);
        return ResultFactory.buildSuccessResult(code);
    }

//    @Autowired
//    CircleCaptchaUtil circleCaptchaUtil;
//    @GetMapping("/test")
//    public String test(){
//        return circleCaptchaUtil.setCircleCaptcha();
//    }

}
