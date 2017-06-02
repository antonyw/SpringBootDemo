package me.anthony.controller;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anthony on 2017/6/1.
 */
@Controller
public class PageController {

    private Logger logger = LoggerFactory.getLogger(getClass().getPackage().getName());

    @RequestMapping("/login.html")
    public String login(HttpServletRequest request, Model model) {
        // 登录失败从request中获取shiro处理的异常信息，shiroLoginFailure:就是shiro异常类的全类名.
        String exception = (String) request.getAttribute("shiroLoginFailure");
        logger.debug("Shiro Exception:" + exception);
        String msg = "";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                logger.debug("UnknownAccountException -- > 账号不存在：");
                msg = "账号不存在";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                logger.debug("IncorrectCredentialsException -- > 密码不正确：");
                msg = "密码不正确";
            } else if ("CaptchaException".equals(exception)) {
                logger.debug("CaptchaException -- > 验证码错误");
                msg = "验证码错误";
            } else {
                msg = "请稍候再试";
                logger.debug("else -- >" + exception);
            }
        }
        model.addAttribute("err", msg);
        return "login";
    }

}
