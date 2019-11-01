package team.huoguo.login.entity.remind;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author GreenHatHG
 **/

@Entity
@Data
public class RemindInfo {

    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    /**
     * 对应UserInfo的id字段
     */
    @Column(nullable = false)
    private String uid;

    /**
     * 提醒时间
     */
    @Column(nullable = false)
    private String remindDate;

    /**
     * 推送方式
     * 1-邮箱
     */
    @Column(nullable = false)
    private int type;

    /**
     * 联系方式
     */
    @Column(nullable = false)
    private String contact;
}
