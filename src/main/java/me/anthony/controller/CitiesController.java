package me.anthony.controller;

import com.alibaba.fastjson.JSON;
import me.anthony.service.CitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anthony on 2017/5/29.
 */
@RestController
@RequestMapping("/area")
public class CitiesController {

    @Autowired
    private CitiesService citiesService;

    @RequestMapping("/all")
    public String all(@RequestParam(defaultValue = "-1") int page, @RequestParam(defaultValue = "-1") int city_id, @RequestParam(defaultValue = "-1") int country_id) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("page", page == -1 ? null : page);
        param.put("city_id", city_id);
        param.put("country_id", country_id);
        return JSON.toJSONString(citiesService.selectWithCountry(param));
    }
}
