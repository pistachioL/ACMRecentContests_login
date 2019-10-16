package team.huoguo.login.bean.rbac;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 用户信息表
 * @author GreenHatHG
 */

@Entity(name = "userinfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class UserInfo implements Serializable {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String mail;

    /**
     * 创建时间
     */
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime;

    /**
     * 一个用户具有多个角色
     * JoinTable 描述了多对多关系的数据表关系，name属性指定中间表名称。
     * name: 额外表的名称
     * joinColumns 定义中间表与此表的外键关系，中间表SysUserRole的uid列是此表的主键列对应的外键列
     * inverseJoinColumns 属性定义了中间表与另外一端(SysRole)的外键关系。
     */
    //立即从数据库中进行加载数据;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "uid") }
        ,inverseJoinColumns ={@JoinColumn(name = "roleId")})
    private Set<SysRole> roleList;
}
