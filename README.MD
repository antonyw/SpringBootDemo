# Spring Boot 集成Web开发常用库
_项目用于记录各三方库的集成方法，故没有采用微服务架构_
#### 项目依赖
- [Spring Boot 1.5.3.RELEASE](https://github.com/spring-projects/spring-boot)
- [thymeleaf](http://www.thymeleaf.org/)
- [MyBatis](http://www.mybatis.org/mybatis-3/)
- [通用Mapper](https://github.com/abel533/Mapper)
- [分页插件](https://github.com/pagehelper/pagehelper-spring-boot)
- [验证码](https://github.com/penggle/kaptcha)
- [Druid 1.0.27](https://github.com/alibaba/druid)
#### 通用Mapper的用法
- 接口方式

通常简单SQL可以使用此类方法，部分复杂SQL可以使用带Example的查询方法。（Example方法可以查看[通用Mapper文档](https://github.com/abel533/Mapper)）
1. 定义实体类User，BaseEntity用于定义分页参数row和page。
```java 
class User extends BaseEntity {
     private Integer id;
     private String username;
     private String password;
   
     //getter and setter
}
```
2. 定义接口，继承自tk.mybatis.mapper
```java
interface UserMapper extends Mapper<User> {
}
```
3. 注入mapper，因为我们使用starter的方式，所以编辑器可能会报错找不到此mapper的bean对象，忽略即可
```java
@Autowired
UserMapper userMapper;
```
- xml方式
1. 定义xml
```xml
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="me.anthony.mapper.CitiesMapper">
    <select id="selectWithCountry" parameterType="java.util.Map" resultType="me.anthony.entity.Cities">
        SELECT c.city,co.country
        FROM cities c
        LEFT JOIN countries co ON c.country_id=co.country_id
        <where>
            <if test="city_id != null and city_id != -1">
                and c.city_id = #{city_id}
            </if>
            <if test="country_id != null and country_id != -1">
                and c.country_id = #{country_id}
            </if>
        </where>
    </select>
</mapper>
```
2. 注入sqlSession
```java
@Autowired
SqlSession sqlSession;
sqlSession.selectList("selectWithCountry");
```