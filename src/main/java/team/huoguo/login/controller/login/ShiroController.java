package team.huoguo.login.controller.login;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.huoguo.login.bean.Result;
import team.huoguo.login.bean.ResultFactory;
import team.huoguo.login.bean.rbac.UserInfo;
import team.huoguo.login.config.shiro.JWTUtil;
import team.huoguo.login.repository.UserRepository;
import team.huoguo.login.utils.Argon2Util;
import team.huoguo.login.utils.RedisUtil;

import java.util.Map;

/**
 * @author GreenHatHG
 */
@RestController
@RequestMapping(value="/api/v1")
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

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, Object> payload) {
        final String errorMessage = "用户名或密码错误";
        JSONObject jsonObject = JSONUtil.parseObj(payload);

        String code = jsonObject.getStr("code");
        String fileName = jsonObject.getStr("imgId");

        if(code != null && !code.toLowerCase().equals(redisUtil.getString(fileName))){
            return ResultFactory.buildFailResult("验证码错误");
        }
        String username = jsonObject.getStr("username");
        String password = jsonObject.getStr("password");

        if(fileName != null){
            redisUtil.deleteKey(fileName);
        }
        UserInfo userInfo = null;
        if(!Validator.isEmail(username)){
            userInfo = userRepository.findByUsername(username);
        }else{
            userInfo = userRepository.findByMail(username);
        }
        if (userInfo == null) {
            return ResultFactory.buildFailResult(errorMessage);
        }
        if (!Argon2Util.verify(userInfo.getPassword(), password)) {
            return ResultFactory.buildFailResult(errorMessage);
        }
        return JWTUtil.generateUserInfo(userInfo);
    }

    @GetMapping("/123")
//    @RequiresAuthentication
    public Result test(@NotNull String test){
        System.out.println(test);
        return ResultFactory.buildSuccessResult("成功");
//        throw new CustomException(1,"1");
    }

}
