package team.huoguo.login.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.huoguo.login.bean.Result;
import team.huoguo.login.bean.UserInfo;
import team.huoguo.login.service.ResultFactory;
import team.huoguo.login.service.UserRepository;
import team.huoguo.login.utils.Argon2Util;
import team.huoguo.login.utils.JWTUtil;
import team.huoguo.login.utils.RedisUtil;

import java.util.Map;

/**
 * @author GreenHatHG
 */

@RestController
public class ShiroController {

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

    @PostMapping("/v1/login")
    public Result login(@RequestBody Map<String, Object> payload) {
        final String errorMessage = "用户名或密码错误";
        JSONObject jsonObject = JSONUtil.parseObj(payload);

        String code = jsonObject.getStr("code");
        String fileName = jsonObject.getStr("fileName");
        System.out.println(code.toLowerCase());
        System.out.println(fileName);
        System.out.println(redisUtil.getString(fileName));
        if(code == null || !code.toLowerCase().equals(redisUtil.getString(fileName))){
            return ResultFactory.buildFailResult("验证码错误");
        }
        String username = jsonObject.getStr("username");
        String password = jsonObject.getStr("password");

        UserInfo userInfo = userRepository.findByUsername(username);
        if (userInfo == null) {
            return ResultFactory.buildFailResult(errorMessage);
        }
        if (!Argon2Util.verify(userInfo.getPassword(), password)) {
            return ResultFactory.buildFailResult(errorMessage);
        }
        return JWTUtil.generateUserInfo(userInfo);
    }

    @RequestMapping(value = "/unauth")
    public Result unauth() {
        return ResultFactory.buildUnauthorizedResult("未登陆");
    }
}
