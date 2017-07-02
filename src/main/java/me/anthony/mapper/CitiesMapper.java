package me.anthony.mapper;

import me.anthony.entity.Cities;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by Anthony on 2017/5/29.
 */
public interface CitiesMapper extends Mapper<Cities> {

    List<Cities> selectWithCountry(Map<String, Object> param);
}
