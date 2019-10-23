package team.huoguo.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.huoguo.login.bean.Result;
import team.huoguo.login.bean.ResultFactory;
import team.huoguo.login.config.shiro.JWTUtil;
import team.huoguo.login.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * @author GreenHatHG
 **/
@RestController
@RequestMapping(value="/api/v1")
public class UserInfoController {

    @Autowired
    private UserRepository userRepository;

    @PutMapping("username")
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

    @PutMapping("city")
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

}
