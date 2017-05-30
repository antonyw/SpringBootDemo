package me.anthony.conf;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Created by Anthony on 2017/5/30.
 */
@WebServlet(urlPatterns = "/druid/*",initParams = {
        @WebInitParam(name = "loginUsername",value = "admin"),
        @WebInitParam(name = "loginPassword",value = "mm123"),
        @WebInitParam(name = "resetEnable",value = "false")
})
public class DruidStatViewServlet extends StatViewServlet {
}
