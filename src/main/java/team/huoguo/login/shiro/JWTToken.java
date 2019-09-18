package team.huoguo.login.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 创建JWTToken替换Shiro原生Token
 * Shiro原生的Token中存在用户名和密码以及其他信息[验证码，记住我]
 * 在JWT的Token中因为已将用户名和密码通过加密处理整合到一个加密串中，所以只需要一个token字段即可
 *
 * @author GreenHatHG
 **/

public class JWTToken implements AuthenticationToken {

    // 密钥
    private String token;

    public JWTToken(String token) {
        if (token.contains("Bearer")) {
            token = token.substring(7, token.length());
        }
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}