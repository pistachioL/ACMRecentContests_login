package team.huoguo.login.bean.rbac;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * 角色信息表
 * @author GreenHatHG
 **/

@Entity
@Data
public class SysRole {

    /**
     * 编号
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 角色标识程序中判断使用,如"admin",这个是唯一的;
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 角色 -- 权限关系：多对多关系;
     */
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="roleId")}
        ,inverseJoinColumns={@JoinColumn(name="permissionId")})
    private Set<SysPermission> permissions;

    /**
     *  一个角色对应多个用户
     */
    @ManyToMany
    @JoinTable(name="SysUserRole",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="uid")})
    private Set<UserInfo> userInfos;
}
