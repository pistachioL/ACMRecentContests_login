package team.huoguo.login.entity.remind;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.huoguo.login.common.utils.MailUtil;
import team.huoguo.login.entity.resp.Result;
import team.huoguo.login.entity.resp.ResultFactory;
import team.huoguo.login.shiro.JWTUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author GreenHatHG
 **/
@RestController
@RequestMapping(value="${API}"+"/remind")
public class RemindController {

    @Autowired
    private RemindRepository remindRepository;

    @Autowired
    private QuartzJobService quartzJobService;

    @Autowired
    private MailUtil mailUtil;

    @PostMapping("remind_info")
    public Result addRemindInfo(HttpServletRequest request,
            @RequestParam @NotNull String contest,
            @RequestParam @NotNull String remindDate,
            @RequestParam int type,
            @RequestParam @NotNull String contact){
        String id = JWTUtil.getId(request.getHeader("Authorization"));
        RemindInfo remindInfo = new RemindInfo();
        remindInfo.setContact(contact);
        remindInfo.setUid(id);
        remindInfo.setRemindDate(remindDate);
        remindInfo.setType(type);

        JSONObject jsonObject = JSONUtil.parseObj(contest);
        try{
            remindRepository.save(remindInfo);
            quartzJobService.scheduleJob(contact, mailUtil.remindMailContent(jsonObject), remindDate);
        }catch (Exception e){
            e.printStackTrace();
            return ResultFactory.buildFailResult("设置定时任务失败，请稍后再试");
        }
        return ResultFactory.buildSuccessResult("设置成功");
    }
}
