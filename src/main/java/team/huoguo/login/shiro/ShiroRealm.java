package team.huoguo.login.shiro;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import team.huoguo.login.bean.rbac.SysPermission;
import team.huoguo.login.bean.rbac.SysRole;
import team.huoguo.login.bean.rbac.UserInfo;
import team.huoguo.login.service.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author GreenHatHG
 */

public class ShiroRealm extends AuthorizingRealm {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 重写Realm的supports()方法是通过JWT进行登录判断的关键
     * 因为前文中创建了JWTToken用于替换 Shiro 原生 token
     * 所以必须在此方法中显式的进行替换，否则在进行判断时会一直失败
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 执行授权逻辑
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = JWTUtil.getUsername(principalCollection.toString());
        UserInfo userInfo = userRepository.findByUsername(username);
        List<String> permissionList = new ArrayList<>();
        List<String> roleNameList = new ArrayList<>();
        Set<SysRole> roleSet = userInfo.getRoleList();

        if (CollectionUtils.isNotEmpty(roleSet)) {
            for (SysRole role : roleSet) {
                // 添加角色
                roleNameList.add(role.getName());
                // 根据用户角色查询权限
                Set<SysPermission> permissionSet = role.getPermissions();
                if (CollectionUtils.isNotEmpty(permissionSet)) {
                    for (SysPermission permission : permissionSet) {
                        // 添加权限
                        permissionList.add(permission.getUrl());
                    }
                }
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissionList);
        info.addRoles(roleNameList);
        return info;
    }

    /**
     * 执行认证逻辑
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     * 登录的合法验证通常包括 token 是否有效 、用户名是否存在 、密码是否正确
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        String userName = JWTUtil.getUsername(token);
        String secert = userRepository.getCredentials(userName);

        /**
         * token为空或者不通过
         */
        if (StringUtils.isBlank(token) || !JWTUtil.verify(token, userName, secert)) {
            throw new AuthenticationException("token校验不通过");
        }

        //认证成功，将用户信息封装成SimpleAuthenticationInfo
        return new SimpleAuthenticationInfo(token, token, "shiroRealm");
    }
}
