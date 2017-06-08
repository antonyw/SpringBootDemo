package me.anthony.conf.shiro;

import me.anthony.entity.User;
import me.anthony.mapper.UserMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by Anthony on 2017/6/8.
 */
public class CustomRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(getClass().getPackage().getName());

    @Autowired
    private UserMapper userMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //鉴权方法
        logger.info("Shiro 鉴权方法");
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //身份认证方法
        logger.info("Shiro 身份认证方法");
        String username = (String) authenticationToken.getPrincipal();
        Example example = new Example(User.class);
        example.createCriteria().andCondition("username=", username);
        List<User> users = userMapper.selectByExample(example);
        if (users == null || users.size() != 1) {
            return null;
        }
        User u = users.get(0);
        return new SimpleAuthenticationInfo(u, u.getPassword(), getName());
    }
}
