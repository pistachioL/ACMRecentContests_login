package team.huoguo.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.huoguo.login.bean.Result;
import team.huoguo.login.bean.UserInfo;
import team.huoguo.login.service.ResultFactory;
import team.huoguo.login.service.UserRepository;
import team.huoguo.login.untils.Argon2Util;

import java.util.Map;

/**
 * @author GreenHatHG
 */

@RestController
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public Result Register(@RequestBody Map<String, String> payload){
        String username = payload.get("username");
        String password = payload.get("password");

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(Argon2Util.hash(password));
        try{
            userRepository.save(userInfo);
        }catch (Exception e){
            e.printStackTrace();
            return ResultFactory.buildFailResult("账号已存在");
        }
        return ResultFactory.buildSuccessResult("成功");
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }

}
