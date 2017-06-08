package me.anthony.controller;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import me.anthony.service.ActorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Anthony on 2017/5/29.
 */
@RestController
@RequestMapping("/user")
public class ActorsController {

    @Autowired
    private ActorsService actorsService;

    @Autowired
    private DefaultKaptcha producer;

    @RequestMapping("/all")
    public String all(@RequestParam(defaultValue = "0") Integer page) {
        return JSON.toJSONString(actorsService.selectAllByPage(page));
    }

    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String text = producer.createText();
        req.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        BufferedImage image = producer.createImage(text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }
}
