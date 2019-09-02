package team.huoguo.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.huoguo.login.bean.Result;
import team.huoguo.login.bean.UserInfo;
import team.huoguo.login.service.ResultFactory;
import team.huoguo.login.service.UserRepository;
import team.huoguo.login.untils.Argon2Util;
import team.huoguo.login.untils.JWTUtil;

import java.util.Map;

/**
 * @author GreenHatHG
 */

@RestController
public class ShiroController {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> payload){
        String username = payload.get("username");
        String password = payload.get("password");
        final String errorMessage = "用户名或密码错误";

        UserInfo userInfo = userRepository.findByUsername(username);
        if(userInfo == null){
            return ResultFactory.buildFailResult(errorMessage);
        }
        if(!Argon2Util.verify(userInfo.getPassword(), password)){
            return ResultFactory.buildFailResult(errorMessage);
        }
        return JWTUtil.generateUserInfo(userInfo);
    }

    @RequestMapping(value = "/unauth")
    public Result unauth() {
        return ResultFactory.buildUnauthorizedResult("未登陆");
    }
}
