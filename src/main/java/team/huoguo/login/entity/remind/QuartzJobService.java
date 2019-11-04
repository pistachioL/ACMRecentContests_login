package team.huoguo.login.entity.remind;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.huoguo.login.common.utils.MailUtil;

import java.util.*;

/**
 * @author GreenHatHG
 **/
@Component
public class QuartzJobService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private MailUtil mailUtil;

    public String addJob(String to, JSONObject contest, String sendDate, String jobGroup){

        if (checkExists(contest.getStr("name"), jobGroup)){
            return "新增任务失败，该任务已存在";
        }

        if(contest.containsKey("startTime")&& contest.getStr("startTime").compareTo(DateUtil.now()) < 0){
            return "操作失败：当前比赛已过期！";
        }

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("content", mailUtil.remindMailContent(contest));
        jobDataMap.put("to", to);

        JSONObject description = new JSONObject();
        description.put("to", to);
        description.put("sendDate", sendDate);
        //创建一个任务
        //指明job的名称，所在组的名称，以及绑定job类
        JobDetail jobDetail = JobBuilder.newJob(MailTask.class)
                .withIdentity(contest.getStr("name"), jobGroup)
                .usingJobData(jobDataMap)
                .build();

        //创建一个触发器，指定任务在什么时间执行
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(contest.getStr("name"), jobGroup)
                .withDescription(description.toString())
                .startAt(DateUtil.parse(sendDate, "yyyy-MM-dd HH:mm:ss")).build();

        // 启动调度
        try{
            scheduler.scheduleJob(jobDetail, trigger);
        }catch (Exception e){
            e.printStackTrace();
            return "启动任务失败，请稍后再试！";
        }

        return "";
    }

    public String editJob(String to, String sendDate, JSONObject contest, String jobGroup){

        if (!checkExists(contest.getStr("name"), jobGroup)) {
            return "查无记录！";
        }

        if(contest.containsKey("startTime")&& contest.getStr("startTime").compareTo(DateUtil.now()) < 0){
            return "操作失败：当前比赛已过期！";
        }

        if(!"".equals(deleteJob(contest.getStr("name"), jobGroup))) {
            return "修改失败，请稍后再试";
        }

        return addJob(to, contest, sendDate, jobGroup);
    }

    public String deleteJob(String jobName, String jobGroup){
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try{
            if (checkExists(jobName, jobGroup)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
            }else{
                return "查无记录！";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "删除失败，请稍后再试！";
        }
        return "";
    }

    public String pauseJob(String jobName, String jobGroup) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.pauseTrigger(triggerKey);
            }else {
                return "查无记录！";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "暂停失败，请稍后再试！";
        }
        return "";
    }

    public String resumeJob(String jobName, String jobGroup){
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.resumeTrigger(triggerKey);
            }else {
                return "查无记录！";
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "重新启动失败，请稍后再试！";
        }
        return "";
    }

    public List<Map<String, String>> selectAllByJobGroup(String jobGroup) throws SchedulerException {
        List<Map<String, String>> list = new ArrayList<>();

        Set<TriggerKey> triggerKeys =  scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(jobGroup));
        for(TriggerKey triggerKey : triggerKeys){
            Map<String, String> map = new HashMap<>(2);
            Trigger trigger = scheduler.getTrigger(triggerKey);
            map.put("name", triggerKey.getName());
            Trigger.TriggerState state = scheduler.getTriggerState(triggerKey);
            map.put("state", "NORMAL".equals(state.name()) ? "正常" : "暂停");
            JSONObject jsonObject = JSONUtil.parseObj(trigger.getDescription());
            map.put("to", jsonObject.getStr("to"));
            map.put("sendDate", jsonObject.getStr("sendDate"));
            map.put("type", "邮箱");
            list.add(map);
        }
        return list;

    }

    public boolean checkExists(String jobName, String jobGroup){
        try{
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            return scheduler.checkExists(triggerKey);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
