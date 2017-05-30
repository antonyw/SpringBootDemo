package me.anthony.service;

import com.github.pagehelper.PageHelper;
import me.anthony.entity.Cities;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Anthony on 2017/5/29.
 */
@Service
public class CitiesService {

    @Autowired
    private SqlSession sqlSession;

    public List<Cities> selectWithCountry(Map<String, Object> param) {
        if (param != null && param.get("page") != null) {
            PageHelper.startPage(Integer.parseInt(param.get("page").toString()), 10);
        }
        return sqlSession.selectList("selectWithCountry", param);
    }

}
