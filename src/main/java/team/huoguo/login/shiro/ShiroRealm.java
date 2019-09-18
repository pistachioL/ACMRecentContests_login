package team.huoguo.login.shiro;

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
import team.huoguo.login.bean.UserInfo;
import team.huoguo.login.exception.CustomException;
import team.huoguo.login.service.UserRepository;
import team.huoguo.login.utils.JWTUtil;

import java.util.ArrayList;
import java.util.List;

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
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<String> userPermissions = new ArrayList<>();
        userPermissions.add("user");
        info.addStringPermissions(userPermissions);
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
        String username = JWTUtil.getUsername(token);

        if (StringUtils.isBlank(username)) {
            throw new CustomException(401, "token检验不通过");
        }

        UserInfo userInfo = userRepository.findByUsername(username);
        if (userInfo == null) {
            throw new CustomException(404, "用户不存在");
        }

        if (!JWTUtil.verify(token, username, userInfo.getPassword())) {
            throw new CustomException(401, "账户或者密码错误");
        }

        //认证成功，将用户信息封装成SimpleAuthenticationInfo
        return new SimpleAuthenticationInfo(token, token, "realm");
    }
}
