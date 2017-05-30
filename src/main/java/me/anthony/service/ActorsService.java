package me.anthony.service;

import com.github.pagehelper.PageHelper;
import me.anthony.entity.Actors;
import me.anthony.mapper.ActorsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Anthony on 2017/5/29.
 */
@Service
public class ActorsService {

    @Autowired
    private ActorsMapper actorsMapper;

    public List<Actors> selectAllByPage(Integer page) {
        if (page != null && page != 0) {
            PageHelper.startPage(page, 10);
        }
        return actorsMapper.selectAll();
    }
}
