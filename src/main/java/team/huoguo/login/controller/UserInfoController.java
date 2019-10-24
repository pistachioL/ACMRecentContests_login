package team.huoguo.login.controller;

import cn.hutool.core.lang.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.huoguo.login.bean.Result;
import team.huoguo.login.bean.ResultFactory;
import team.huoguo.login.config.shiro.JWTUtil;
import team.huoguo.login.repository.UserRepository;
import team.huoguo.login.service.MailService;
import team.huoguo.login.utils.Argon2Util;
import team.huoguo.login.utils.RedisUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author GreenHatHG
 **/
@RestController
@RequestMapping(value="${API}"+"/userinfo")
public class UserInfoController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private RedisUtil redisUtil;

    @PutMapping("/username")
    public Result updateUserName(HttpServletRequest request, @NotNull String username){
        String id = JWTUtil.getId(request.getHeader("Authorization"));
        if(!userRepository.findById(id).isPresent()){
            return ResultFactory.buildFailResult("查无此人");
        }
        try{
            userRepository.updateUsernameById(username, id);
        }catch (Exception e){
            e.printStackTrace();
            return ResultFactory.buildFailResult("更新失败-->" + e.getMessage());
        }
        return ResultFactory.buildSuccessResult("成功");
    }

    @PutMapping("/city")
    public Result updateCity(HttpServletRequest request, @NotNull String city){
        String id = JWTUtil.getId(request.getHeader("Authorization"));
        if(!userRepository.findById(id).isPresent()){
            return ResultFactory.buildFailResult("查无此人");
        }
        try{
            userRepository.updateCityById(city, id);
        }catch (Exception e){
            e.printStackTrace();
            return ResultFactory.buildFailResult("更新失败-->" + e.getMessage());
        }
        return ResultFactory.buildSuccessResult("成功");
    }

    @GetMapping("emailcode")
    public Result getEmailVerificationCode(HttpServletRequest request,@NotNull String originalEmail){
         if(!Validator.isEmail(originalEmail)) {
             return ResultFactory.buildFailResult("邮箱格式不对");
         }
        String id = JWTUtil.getId(request.getHeader("Authorization"));
        String dbEmail = userRepository.findEmailById(id);
        if(dbEmail == null || !dbEmail.equals(originalEmail)){
            return ResultFactory.buildFailResult("输入邮箱和原邮箱不一致");
        }
        if(redisUtil.tooManyTimes(originalEmail)){
            return ResultFactory.buildFailResult("该邮箱发送次数，请稍后再试");
        }
        String code = mailService.getCode();
        System.out.println("code:  "+code);
//        mailService.sendMail(originalEmail, code);
        redisUtil.setString(originalEmail+"updateEmail", code);
        return ResultFactory.buildSuccessResult("成功");
    }

    @PostMapping("updateEmail")
    public Result updateEmail(HttpServletRequest request,
                              @NotNull String originalEmail, @NotNull  String code, @NotNull String newEmail){
        Object redisCode = redisUtil.getString(originalEmail+"updateEmail");
        if(redisCode == null || !redisCode.toString().equals(code)){
            return ResultFactory.buildFailResult("验证码已过期");
        }
        if(!Validator.isEmail(newEmail)){
            return ResultFactory.buildFailResult("邮箱格式不对");
        }
        if(userRepository.findByMail(newEmail) != null){
            return ResultFactory.buildFailResult("邮箱已存在");
        }
        redisUtil.deleteKey(originalEmail+"updateEmail");
        try{
            userRepository.updateEmailById(newEmail, JWTUtil.getId(request.getHeader("Authorization")));
        }catch (Exception e){
            e.printStackTrace();
            return ResultFactory.buildFailResult("更新失败，请联系管理员");
        }
        return ResultFactory.buildSuccessResult("成功");
    }

    @PostMapping("updatePassword")
    public Result updatePassword(HttpServletRequest request,
                                 @NotNull String email, @NotNull String code, @NotNull String password){
        String id = JWTUtil.getId(request.getHeader("Authorization"));
        if(!userRepository.findById(id).isPresent()){
            return ResultFactory.buildFailResult("查无此人");
        }
        if(!code.toLowerCase().equals(redisUtil.getString(email+"updateEmail"))){
            return ResultFactory.buildFailResult("验证码错误");
        }
        redisUtil.deleteKey(email+"updateEmail");
        userRepository.updatePasswordById(Argon2Util.hash(password), id);
        return ResultFactory.buildSuccessResult("成功");
    }

}
