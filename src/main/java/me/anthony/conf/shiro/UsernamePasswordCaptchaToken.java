package me.anthony.conf.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Created by Anthony on 2017/6/2.
 */
public class UsernamePasswordCaptchaToken extends UsernamePasswordToken {

    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public UsernamePasswordCaptchaToken() {
        super();
    }

    public UsernamePasswordCaptchaToken(String username, String password, boolean rememberMe, String host, String captcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
    }
}
