package team.huoguo.login.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.huoguo.login.bean.Result;
import team.huoguo.login.bean.UserInfo;
import team.huoguo.login.service.ResultFactory;

/**
 * @author GreenHatHG
 */

@RestController
public class ShiroController {

    @PostMapping("/login")
    public Result login(@RequestBody UserInfo userInfo){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userInfo.getUsername(), userInfo.getPassword());

        try{
            subject.login(token);
            return ResultFactory.buildSuccessResult((UserInfo)subject);
        } catch (IncorrectCredentialsException e) {
            return ResultFactory.buildFailResult("密码错误");
        } catch (DisabledAccountException e) {
            return ResultFactory.buildFailResult("登录失败，该用户不可用");
        } catch (AuthenticationException e) {
            return ResultFactory.buildFailResult("该用户不存在");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultFactory.buildFailResult("登陆失败");
    }


    @RequestMapping(value = "/unauth")
    public Result unauth() {
        return ResultFactory.buildUnauthorizedResult("未登陆");
    }
}
