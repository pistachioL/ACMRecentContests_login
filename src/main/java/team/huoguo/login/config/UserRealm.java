package team.huoguo.login.config;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import team.huoguo.login.bean.UserInfo;
import team.huoguo.login.service.UserRepository;

/**
 * @author GreenHatHG
 */
public class UserRealm extends AuthorizingRealm{

    @Autowired
    private UserRepository userRepository;

    /**
     * 执行授权逻辑
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 执行认证逻辑
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //编写shiro判断逻辑，判断用户名和密码
        UsernamePasswordToken token  =  (UsernamePasswordToken)authenticationToken;
        UserInfo user = userRepository.findByUsername(token.getUsername());

        //判断用户名
        if(user == null){
            /**
             * 用户名不存在
             * shiro底层会抛出UnKnowAccountException
             */
            return null;
        }

        //判断密码, 这里的user是principal
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }
}
