package me.anthony.conf;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * Created by Anthony on 2017/5/30.
 */
@WebFilter(filterName = "druidWebFilter", urlPatterns = "/*", initParams = {
        @WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*")
})
public class DruidStatFilter extends WebStatFilter {
}
