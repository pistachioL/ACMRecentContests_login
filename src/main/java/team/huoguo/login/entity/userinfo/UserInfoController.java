package team.huoguo.login.entity.userinfo;

import cn.hutool.core.lang.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.huoguo.login.common.GlobalConst;
import team.huoguo.login.common.utils.Argon2Util;
import team.huoguo.login.common.utils.FileHandleUtil;
import team.huoguo.login.common.utils.MailUtil;
import team.huoguo.login.common.utils.RedisUtil;
import team.huoguo.login.entity.resp.Result;
import team.huoguo.login.entity.resp.ResultFactory;
import team.huoguo.login.shiro.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author GreenHatHG
 **/
@RestController
@RequestMapping(value="${API}"+"/userinfo")
@Validated
public class UserInfoController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 用户头像文件存放路径
     */
    private static final String BASE_PATH = "resources/avatar/";

    @PutMapping("/username")
    public Result updateUserName(HttpServletRequest request,
                                 @NotBlank @Size(max = 30) String username){
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
    public Result updateCity(HttpServletRequest request,
                             @NotBlank @Size(max = 30) String city){
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
    public Result getEmailVerificationCode(HttpServletRequest request,
                                           @NotBlank @Size(max = 30) String originalEmail){
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
        String code = mailUtil.getCode();
        System.out.println("code:  "+code);
        if(GlobalConst.sendMail){
            mailUtil.sendMail(originalEmail, code);
        }
        redisUtil.setString(originalEmail+"updateEmail", code);
        return ResultFactory.buildSuccessResult("成功");
    }

    @PutMapping("email")
    public Result updateEmail(HttpServletRequest request,
                              @NotBlank @Size(max = 30) String originalEmail,
                              @NotBlank @Size(max = 30)  String code,
                              @NotBlank @Size(max = 30) String newEmail){
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

    @PutMapping("password")
    public Result updatePassword(HttpServletRequest request,
                                 @NotBlank @Size(max = 30) String email,
                                 @NotBlank @Size(max = 30) String code,
                                 @NotBlank @Size(max = 30) String password){
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

    @PutMapping("avatar")
    public Result updateAvatar(HttpServletRequest request,
                               @NotBlank @Size(max = 100) String avatarUrl){
        String id = JWTUtil.getId(request.getHeader("Authorization"));
        if(!userRepository.findById(id).isPresent()){
            return ResultFactory.buildFailResult("查无此人");
        }
        try{
            userRepository.updateAvatarById(avatarUrl, id);
        }catch (Exception e){
            e.printStackTrace();
            return ResultFactory.buildFailResult("更新失败-->" + e.getMessage());
        }
        try{
            FileHandleUtil.saveAvatarFromUrl(avatarUrl, id);
        }catch (Exception e){
            e.printStackTrace();
            return ResultFactory.buildFailResult("保存失败-->" + e.getMessage());
        }
        return ResultFactory.buildSuccessResult("成功");
    }

}
