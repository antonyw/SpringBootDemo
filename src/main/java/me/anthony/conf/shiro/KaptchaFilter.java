package me.anthony.conf.shiro;

import com.google.code.kaptcha.Constants;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anthony on 2017/6/2.
 */
public class KaptchaFilter extends FormAuthenticationFilter {

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        UsernamePasswordCaptchaToken token = createToken(request, response);
        String username = token.getUsername();
        try {
            doValidateCaptcha((HttpServletRequest) request, token);
            Subject subject = getSubject(request, response);
            subject.login(token);//正常验证
            //到这里就算验证成功了,把用户信息放到session中
            ((HttpServletRequest) request).getSession().setAttribute("name", username);

            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    private void doValidateCaptcha(HttpServletRequest request, UsernamePasswordCaptchaToken token) {
        String captcha = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (captcha == null || !captcha.equalsIgnoreCase(token.getCaptcha())) {
            throw new CaptchaException("验证码错误");
        }
    }

    @Override
    protected UsernamePasswordCaptchaToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        String captcha = getCaptcha(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        return new UsernamePasswordCaptchaToken(username, password, rememberMe, host, captcha);
    }

    private String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, "captcha"); //对应页面输入验证码的input的name值
    }

    //保存异常对象到request
    @Override
    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        request.setAttribute(getFailureKeyAttribute(), ae);
    }
}
