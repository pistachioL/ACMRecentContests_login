package team.huoguo.login.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author GreenHatHG
 */

@Entity(name = "userinfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class UserInfo implements Serializable {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(nullable = false, unique=true)
    private String username;

    @Column(nullable = false)
    private String password;
}
