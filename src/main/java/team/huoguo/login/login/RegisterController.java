package team.huoguo.login.login;

import cn.hutool.core.lang.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.huoguo.login.common.GlobalConst;
import team.huoguo.login.entity.resp.Result;
import team.huoguo.login.entity.userinfo.UserInfo;
import team.huoguo.login.common.utils.MailUtil;
import team.huoguo.login.entity.resp.ResultFactory;
import team.huoguo.login.entity.userinfo.UserRepository;
import team.huoguo.login.common.utils.Argon2Util;
import team.huoguo.login.common.utils.RedisUtil;

import java.util.Map;

/**
 * @author GreenHatHG
 */
@RestController
@RequestMapping(value="/api/v1")
public class RegisterController {

    private UserRepository userRepository;
    private RedisUtil redisUtil;
    private MailUtil mailUtil;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Autowired
    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
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
        String code = mailUtil.getCode();
        System.out.println("code:  "+code);
        if(GlobalConst.sendMail){
            mailUtil.sendMail(mail, code);
        }

        //重复申请的话，会重新生成code,3分钟内最多5次
        if("register".equals(type)){
            redisUtil.setString(username, code);
        }else if("resetpwd".equals(type)){
            redisUtil.setString(mail+"resetpwd", code);
        }
        return ResultFactory.buildSuccessResult("成功");
    }

}
