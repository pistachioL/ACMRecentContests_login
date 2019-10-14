package team.huoguo.login.shiro;

import cn.hutool.json.JSONUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import team.huoguo.login.bean.Result;
import team.huoguo.login.service.ResultFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 创建JWTFilter实现前端请求统一拦截及处理
 * 所有的请求都会先经过 Filter，所以我们继承官方的 BasicHttpAuthenticationFilter
 * 并且重写鉴权的方法
 * 代码的执行流程 preHandle -> isAccessAllowed -> isLoginAttempt -> executeLogin
 *
 * @author GreenHatHG
 **/
public class JWTFilter extends BasicHttpAuthenticationFilter {

    /**
     * 登录标识
     */
    private static String LOGIN_SIGN = "Authorization";

    /**
     * 检测用户是否登录
     * 检测header里面是否包含Authorization字段即可
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(LOGIN_SIGN);
        return authorization != null;
    }

    /**
     * executeLogin() 方法中的 getSubject(request, response).login(token)
     * 就是触发 Shiro Realm 自身的登录控制，具体内容需要手动实现
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(LOGIN_SIGN);
        JWTToken token = new JWTToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }


    /**
     * 一般在isAccessAllowed中执行认证逻辑
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue){
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
            } catch (Exception e) {
                Result result = ResultFactory.buildFailResult("token检验失败");
                try {
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().println(JSONUtil.parseObj(result));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        Subject subject = getSubject(request, response);
        return subject.isAuthenticated();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        this.sendChallenge(request, response);
        return false;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    //TODO 此处为AccessToken刷新，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
}
