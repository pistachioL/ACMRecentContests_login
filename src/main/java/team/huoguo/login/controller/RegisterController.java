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

/**
 * @author GreenHatHG
 */

@RestController
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public Result Register(@RequestBody UserInfo userInfo){
        try{
            userRepository.save(userInfo);
        }catch (Exception e){
            return ResultFactory.buildFailResult("账号已存在");
        }
        return ResultFactory.buildSuccessResult(userInfo);
    }

    @GetMapping("test")
    public String test(){
        return "1";
    }

}
