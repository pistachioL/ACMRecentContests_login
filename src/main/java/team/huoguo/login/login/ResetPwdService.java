package team.huoguo.login.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.huoguo.login.common.utils.Argon2Util;
import team.huoguo.login.common.utils.RedisUtil;
import team.huoguo.login.entity.resp.Result;
import team.huoguo.login.entity.resp.ResultFactory;
import team.huoguo.login.entity.userinfo.UserRepository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author GreenHatHG
 **/

@RestController
@RequestMapping(value="/api/v1")
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

    @PostMapping("/resetpwd")
    public Result reset(@RequestParam @NotBlank @Size(max = 30) String mail,
                        @RequestParam @NotBlank @Size(max = 30) String code,
                        @RequestParam @NotBlank @Size(max = 30) String password){
        if(!code.toLowerCase().equals(redisUtil.getString(mail+"resetpwd"))){
            return ResultFactory.buildFailResult("验证码错误");
        }
        redisUtil.deleteKey(mail+"resetpwd");
        userRepository.updatePasswordByEmail(mail, Argon2Util.hash(password));
        return ResultFactory.buildSuccessResult("成功");
    }

}
