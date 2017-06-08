package me.anthony.conf.shiro;

import com.google.code.kaptcha.Constants;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anthony on 2017/6/8.
 */
public class CaptchaFilter extends FormAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(getClass().getPackage().getName());

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        UsernamePasswordCaptchaToken token = createToken(request, response);
        try {
            doCaptchaValidate((HttpServletRequest) request, token);
            Subject subject = getSubject(request, response);
            subject.login(token);//正常验证
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    // 验证码校验
    private void doCaptchaValidate(HttpServletRequest request, UsernamePasswordCaptchaToken token) {
        String captcha = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        logger.info("captcha:" + captcha);
        //比对
        if (captcha == null || !captcha.equalsIgnoreCase(token.getCaptcha())) {
            throw new CaptchaException("验证码错误");
        }
    }

    @Override
    protected UsernamePasswordCaptchaToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        String captcha = getCaptcha(request);
        return new UsernamePasswordCaptchaToken(username, password, rememberMe, host, captcha);
    }

    private String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, "captcha");//对应HTML页面中验证码输入框的name
    }
}
