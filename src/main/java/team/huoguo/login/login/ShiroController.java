package team.huoguo.login.login;

import cn.hutool.core.lang.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.huoguo.login.common.utils.Argon2Util;
import team.huoguo.login.common.utils.RedisUtil;
import team.huoguo.login.entity.resp.Result;
import team.huoguo.login.entity.resp.ResultFactory;
import team.huoguo.login.entity.userinfo.UserInfo;
import team.huoguo.login.entity.userinfo.UserRepository;
import team.huoguo.login.shiro.JWTUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    public Result login(@RequestParam @NotBlank @Size(max = 30) String code,
                        @RequestParam @NotBlank @Size(max = 30) String imgId,
                        @RequestParam @NotBlank @Size(max = 30) String username,
                        @RequestParam @NotBlank @Size(max = 30) String password) {
        final String errorMessage = "用户名或密码错误";

        if(code != null && !code.toLowerCase().equals(redisUtil.getString(imgId))){
            return ResultFactory.buildFailResult("验证码错误");
        }

        redisUtil.deleteKey(imgId);
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
