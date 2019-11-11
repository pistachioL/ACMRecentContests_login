package team.huoguo.login.entity.remind;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import team.huoguo.login.common.utils.MailUtil;

/**
 * @author GreenHatHG
 **/

@Component
public class MailTask extends QuartzJobBean{
    @Autowired
    private MailUtil mailUtil;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        mailUtil.remindMail(jobDataMap.getString("to"), jobDataMap.getString("content"));
    }
}
