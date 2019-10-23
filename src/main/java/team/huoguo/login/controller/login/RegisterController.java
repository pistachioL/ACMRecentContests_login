package team.huoguo.login.controller.login;

import cn.hutool.core.lang.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.huoguo.login.bean.Result;
import team.huoguo.login.bean.rbac.UserInfo;
import team.huoguo.login.service.MailService;
import team.huoguo.login.bean.ResultFactory;
import team.huoguo.login.repository.UserRepository;
import team.huoguo.login.utils.Argon2Util;
import team.huoguo.login.utils.RedisUtil;

import java.util.Map;

/**
 * @author GreenHatHG
 */
@RestController
@RequestMapping(value="/api/v1")
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

    @PostMapping("/register")
    public Result register(@RequestBody UserInfo payload, @RequestParam(name = "code") String code) {
        String username = payload.getUsername();
        Object redisCode = redisUtil.getString(username);
        if(redisCode == null || !redisCode.toString().equals(code)){
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

        redisUtil.deleteKey(username);
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

    @PostMapping("/code")
    public Result getCode(@RequestBody Map<String, String> payload){
        String username = payload.get("username");
        String mail = payload.get("mail");
        String type = payload.get("type");
        if(!Validator.isEmail(mail)){
            return ResultFactory.buildFailResult("邮箱格式不对");
        }
        UserInfo userInfo = userRepository.findByMail(mail);
        if("register".equals(type) && userInfo != null){
            return ResultFactory.buildFailResult("邮箱已存在");
        }
        else if("restpwd".equals(type) && userInfo == null){
            return ResultFactory.buildFailResult("邮箱不存在");
        }
        if(redisUtil.tooManyTimes(mail)){
            return ResultFactory.buildFailResult("该邮箱发送次数，请稍后再试");
        }
        String code = mailService.getCode();
        System.out.println("code:  "+code);
//        mailService.sendMail(mail, code);
        //重复申请的话，会重新生成code,十分钟内最多三次
        if("register".equals(type)){
            redisUtil.setString(username, code);
        }else if("resetpwd".equals(type)){
            redisUtil.setString(mail+"resetpwd", code);
        }
        return ResultFactory.buildSuccessResult(code);
    }

}
