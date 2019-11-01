package team.huoguo.login.entity.remind;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author GreenHatHG
 **/

@Component
public class QuartzJobService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 创建和启动定时任务
     */
    public void scheduleJob(String to, String context, String sendDate) throws SchedulerException {
        String jobKey = IdUtil.simpleUUID();
        String triggerKey = IdUtil.simpleUUID();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("content", context);
        jobDataMap.put("to", to);
        //创建一个任务
        //指明job的名称，所在组的名称，以及绑定job类
        JobDetail jobDetail = JobBuilder.newJob(MailTask.class)
                .withIdentity(jobKey, MailTask.class.getName())
                .usingJobData(jobDataMap)
                .build();

        //创建一个触发器，指定任务在什么时间执行
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey, MailTask.class.getName())
                .startAt(DateUtil.parse(sendDate, "yyyy-MM-dd HH:mm:ss")).build();

        // 启动调度
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
