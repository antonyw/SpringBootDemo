package me.anthony.conf.shiro;

import me.anthony.entity.User;
import me.anthony.mapper.UserMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.Filter;
import java.util.List;
import java.util.Map;

/**
 * Created by Anthony on 2017/6/2.
 */
@Configuration
public class ShiroConfig {

    private Logger logger = LoggerFactory.getLogger(getClass().getPackage().getName());

    @Autowired
    private UserMapper userMapper;

    @Bean
    public Realm realm() {
        AuthorizingRealm realm = new AuthorizingRealm() {
            @Override
            protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
                logger.info("Shiro 身份验证");
                String username = (String) authenticationToken.getPrincipal();
                Example example = new Example(User.class);
                example.createCriteria().andCondition("username = ", username);
                List<User> list = userMapper.selectByExample(example);
                User userInfo = list.size() == 1 ? list.get(0) : null;
                if (userInfo != null) {
                    return new SimpleAuthenticationInfo(
                            userInfo, //用户名
                            userInfo.getPassword(), //密码
                            getName()  //realm name
                    );
                } else {
                    throw new UnknownAccountException();
                }

            }

            @Override
            protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
                logger.info("Shiro 权限验证");
                if (!SecurityUtils.getSubject().isAuthenticated()) {
                    doClearCache(principalCollection);
                    SecurityUtils.getSubject().logout();
                    return null;
                }
                return null;
            }
        };
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 md5(md5(""));

        return hashedCredentialsMatcher;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/login.html", "authc"); // need to accept POSTs from the login form
        chainDefinition.addPathDefinition("/logout", "logout");
        return chainDefinition;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new KaptchaFilter());
        return filterRegistrationBean;
    }
}
