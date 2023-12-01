package com.segroup.seproject_backend.controller;

import com.segroup.seproject_backend.data_item.*;
import com.segroup.seproject_backend.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//这个类用来处理前端对模型列表、数据集列表的请求。
@Controller
public class QueryController {
    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private ProjectRepo repo;

    //对应接口：应用模型页面-请求模型列表
    @PostMapping("/request/model")
    @ResponseBody
    @CrossOrigin
    public QueryModelWebItem handleModelQuery() {
        QueryModelWebItem res = new QueryModelWebItem();
        List<ModelDBItem> models;
        ModelDBItem cmodel = null;
        try{
            models = jdbc.query("SELECT * FROM models",
                    new BeanPropertyRowMapper<>(ModelDBItem.class));
            cmodel = jdbc.queryForObject("SELECT * FROM models WHERE is_active = 1",
                    new BeanPropertyRowMapper<>(ModelDBItem.class));
        }
        catch (EmptyResultDataAccessException e){
            models = null;
        }
        res.setModels(models);
        res.setCurrent_model(cmodel);
        return res;
    }

    //对应接口：训练模型页面-请求数据集列表、请求数据集列表
    @PostMapping("/request/dataset")
    @ResponseBody
    @CrossOrigin
    public QueryDatasetWebItem handleDatasetQuery() {
//        System.out.println("已获取数据集初始化请求");
        // 需要你们来实现
        QueryDatasetWebItem res = new QueryDatasetWebItem();
        List<DatasetDBItem> datasets;

        try{
            datasets = jdbc.query("SELECT * FROM datasets",
                    new BeanPropertyRowMapper<>(DatasetDBItem.class));
        }
        catch (EmptyResultDataAccessException e){
            datasets = null;
        }
        res.setDatasets(datasets);
//        System.out.println(res.getDatasets().get(0));
        return res;
    }

    @PostMapping("/request/usage")
    @ResponseBody
    @CrossOrigin
    public QueryUsageJ2FWebItem handleUsageQuery(QueryUsageF2JWebItem body) {
        return new QueryUsageJ2FWebItem(
            repo.findUsagesByModelId(Long.parseLong(body.getModel_id()))
        );
    }
}