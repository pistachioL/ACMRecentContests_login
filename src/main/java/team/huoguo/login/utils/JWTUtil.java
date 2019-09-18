package team.huoguo.login.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import team.huoguo.login.bean.Result;
import team.huoguo.login.bean.UserInfo;
import team.huoguo.login.service.ResultFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT加密，校验工具
 *
 * @author GreenHatHG
 **/

@Component
public class JWTUtil {

    // 过期时间30天
    private static final long EXPIRE_TIME = 24 * 60 * 30 * 1000;

    /**
     * 校验token是否正确
     *
     * @param token    密钥
     * @param username 登录名
     * @param password 密码
     * @return
     */
    public static boolean verify(String token, String username, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 生成前端需要的用户信息，包括：
     * 1. token
     * 2. userInfo
     *
     * @param userInfo
     * @return
     */
    public static Result generateUserInfo(UserInfo userInfo) {
        Map<String, Object> responseBean = new HashMap<>(2);
        String token = sign(userInfo.getUsername(), userInfo.getPassword());
        responseBean.put("token", token);
        userInfo.setPassword("");
        responseBean.put("userInfo", userInfo);
        return ResultFactory.buildSuccessResult(responseBean);
    }

}
