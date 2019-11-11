package team.huoguo.login.shiro.rbac;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 权限信息表
 * @author GreenHatHG
 **/

@Entity
@Data
public class SysPermission implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 资源路径
     */
    private String url;

    @ManyToMany
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="permissionId")},inverseJoinColumns={@JoinColumn(name="roleId")})
    private Set<SysRole> roles;
}
