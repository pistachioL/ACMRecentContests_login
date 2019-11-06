package team.huoguo.login.entity.remind;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.huoguo.login.entity.resp.Result;
import team.huoguo.login.entity.resp.ResultFactory;
import team.huoguo.login.shiro.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author GreenHatHG
 **/
@RestController
@RequestMapping(value="${API}"+"/remind")
public class RemindController {

    @Autowired
    private QuartzJobService quartzJobService;

    @PostMapping("/remind_info")
    public Result addRemind(HttpServletRequest request,
            @RequestParam @NotNull String contest,
            @RequestParam @NotNull String remindDate,
            @RequestParam int type,
            @RequestParam @NotNull String contact){
        String id = JWTUtil.getId(request.getHeader("Authorization"));
        JSONObject jsonObject = JSONUtil.parseObj(contest);
        String s = null;
        try{
            s = quartzJobService.addJob(contact, jsonObject, remindDate, id);
        }catch (Exception e){
            e.printStackTrace();
            return ResultFactory.buildFailResult("设置定时任务失败，请稍后再试");
        }

        if(!"".equals(s)) {
            return ResultFactory.buildFailResult(s);
        }
        return ResultFactory.buildSuccessResult("设置成功");
    }

    @GetMapping("/remind_infos")
    public Result getRemindInfoById(HttpServletRequest request){
        String id = JWTUtil.getId(request.getHeader("Authorization"));
        List<Map<String, String>> list = null;
        try{
            list = quartzJobService.selectAllByJobGroup(id);
        }catch (Exception e){
            return ResultFactory.buildFailResult("查询失败,请稍后再试");
        }
        return ResultFactory.buildSuccessResult(list);
    }

    @DeleteMapping("/name")
    public Result deleteRemindInfoByName(HttpServletRequest request,
                                         @RequestParam @NotNull String name){
        String id = JWTUtil.getId(request.getHeader("Authorization"));
        String s = quartzJobService.deleteJob(name, id);
        if(!"".equals(s)){
            return ResultFactory.buildFailResult(s);
        }

        return ResultFactory.buildSuccessResult("删除成功");
    }

    @DeleteMapping("/names")
    public Result deleteRemindInfoByNames(HttpServletRequest request,
                                         @RequestBody @NotNull JSONObject jsonObject){
        String id = JWTUtil.getId(request.getHeader("Authorization"));
        JSONArray names = jsonObject.getJSONArray("names");
        for(Object name : names){
            String s = quartzJobService.deleteJob(name.toString(), id);
            if(!"".equals(s)){
                return ResultFactory.buildFailResult(s);
            }
        }
        return ResultFactory.buildSuccessResult("删除成功");
    }

    @PutMapping("/info")
    public Result updateRemindInfo(HttpServletRequest request,
                    @RequestParam @NotNull String contest,
                    @RequestParam @NotNull String remindDate,
                    @RequestParam int type,
                    @RequestParam @NotNull String contact){
        String id = JWTUtil.getId(request.getHeader("Authorization"));
        String s = quartzJobService.editJob(contact, remindDate, JSONUtil.parseObj(contest), id);
        if(!"".equals(s)){
            return ResultFactory.buildFailResult(s);
        }

        return ResultFactory.buildSuccessResult("更新成功");
    }

    @PostMapping("/pause")
    public Result pauseRemind(HttpServletRequest request,
                              @RequestParam @NotNull String name){
        String id = JWTUtil.getId(request.getHeader("Authorization"));
        String s = quartzJobService.pauseJob(name, id);
        if(!"".equals(s)){
            return ResultFactory.buildFailResult(s);
        }

        return ResultFactory.buildSuccessResult("暂停成功");
    }

    @PostMapping("/resume")
    public Result resumeRemind(HttpServletRequest request,
                              @RequestParam @NotNull String name){
        String id = JWTUtil.getId(request.getHeader("Authorization"));
        String s = quartzJobService.resumeJob(name, id);
        if(!"".equals(s)){
            return ResultFactory.buildFailResult(s);
        }

        return ResultFactory.buildSuccessResult("重新启动成功");
    }
}
