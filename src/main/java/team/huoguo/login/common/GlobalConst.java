package team.huoguo.login.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author GreenHatHG
 **/

@Component
public class GlobalConst {

    public static boolean sendMail;

    @Value("${SEND_MAIL}")
    public void setSendMail(boolean sendMail) {
        GlobalConst.sendMail = sendMail;
    }
}
