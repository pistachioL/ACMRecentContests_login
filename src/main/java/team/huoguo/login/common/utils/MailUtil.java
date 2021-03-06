package team.huoguo.login.common.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 发邮件服务
 * @author GreenHatHG
 **/

@Component
public class MailUtil {

    /**
     * 发送邮件的邮箱地址
     */
    private final String FROM = "acmrecentcontents@aliyun.com";

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendSimpleMail(String to, String subject, String content) {
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(FROM);
        //邮件接收人
        message.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        //发送邮件
        mailSender.send(message);
    }

    /**
     * html邮件
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendHtmlMail(String to, String subject, String content) {
        //获取MimeMessage对象
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true);
            //邮件发送人
            messageHelper.setFrom(FROM);
            //邮件接收人
            messageHelper.setTo(to);
            //邮件主题
            message.setSubject(subject);
            //邮件内容，html格式
            messageHelper.setText(content, true);
            //发送
            mailSender.send(message);
            System.out.println("邮件已发送到" + to);
        } catch (MessagingException e) {
            System.out.println("邮件发送到" + to + "异常");
        }
    }

    /**
     * @return 6位验证码
     */
    public String getCode(){
        return RandomUtil.randomNumbers(6).toString();
    }

    public void sendMail(String to, String code){
        String subject = "ACM赛事提醒与管理平台";
        String s = "\n" + "非常感谢注册ACM赛事提醒与管理平台,你的支持是对我们最大的鼓励!" + "\n" + "验证码过期时间：3分钟";
        String content = "验证码：" + code + s;
        sendSimpleMail(to, subject, content);
    }

    public void remindMail(String to, String content){
        String subject = "ACM赛事提醒与管理平台";
        sendSimpleMail(to, subject, content);
    }

    public String remindMailContent(JSONObject jsonObject){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("你设置的提醒：\n");
        stringBuilder.append("比赛平台:");
        stringBuilder.append(jsonObject.getStr("oj"));
        stringBuilder.append("\n");
        stringBuilder.append("比赛名字:");
        stringBuilder.append(jsonObject.getStr("name"));
        stringBuilder.append("\n");
        stringBuilder.append("比赛开始时间:");
        stringBuilder.append(jsonObject.getStr("startTime"));
        stringBuilder.append("\n");
        stringBuilder.append("比赛结束时间:");
        stringBuilder.append(jsonObject.getStr("endTime"));
        stringBuilder.append("\n");
        stringBuilder.append("比赛链接:");
        stringBuilder.append(jsonObject.getStr("link"));
        stringBuilder.append("\n");
        stringBuilder.append("祝你比赛成功，AK全场!");
        return stringBuilder.toString();
    }

}
