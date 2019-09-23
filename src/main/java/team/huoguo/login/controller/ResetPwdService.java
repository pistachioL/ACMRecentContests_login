package team.huoguo.login.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.huoguo.login.bean.Result;
import team.huoguo.login.service.ResultFactory;
import team.huoguo.login.service.UserRepository;
import team.huoguo.login.utils.Argon2Util;
import team.huoguo.login.utils.RedisUtil;

import java.util.Map;

/**
 * @author GreenHatHG
 **/

@RestController
@RequestMapping(value="/api")
public class ResetPwdService {

    private UserRepository userRepository;
    private RedisUtil redisUtil;
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @PostMapping("/v1/resetpwd")
    public Result reset(@RequestBody Map<String, Object> payload){
        JSONObject jsonObject = JSONUtil.parseObj(payload);
        String mail = jsonObject.getStr("mail");
        String code = jsonObject.getStr("code");
        String password = jsonObject.getStr("password");
        if(code == null){
            return ResultFactory.buildFailResult("验证码为空");
        }
        else if(!code.toLowerCase().equals(redisUtil.getString(mail+"resetpwd"))){
            return ResultFactory.buildFailResult("验证码错误");
        }
        redisUtil.deleteKey(mail+"resetpwd");
        userRepository.updatePasswordByEmail(mail, Argon2Util.hash(password));
        return ResultFactory.buildSuccessResult("成功");
    }

}
