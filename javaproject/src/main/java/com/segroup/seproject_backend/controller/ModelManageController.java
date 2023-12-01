package com.segroup.seproject_backend.controller;

import com.segroup.seproject_backend.data_item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import com.segroup.seproject_backend.data_item.ResultWebItem;
import com.segroup.seproject_backend.data_item.SwitchModelF2JWebItem;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.List;

//这个类用来处理模型管理相关的功能
@Controller
public class ModelManageController {
    @Autowired
    private JdbcTemplate jdbc;

    // 用于发送http请求
    @Autowired
    private RestTemplate restTemplate;

    //用于获取配置文件
    @Autowired
    private Environment environment;

    //对应接口：应用模型页面-更换/应用模型
    @PostMapping("/manage/switch_model")
    @ResponseBody
    @CrossOrigin
    public ResultWebItem handleSwitchModel(SwitchModelF2JWebItem body) {
        // 需要你们来实现
        System.out.println(body);

        String path = jdbc.queryForObject("SELECT model_path FROM models WHERE model_id = ?",
                String.class,
                body.getModel_id());
        SwitchModelJ2PWebItem item = new SwitchModelJ2PWebItem(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("new_model_path", item.getNew_model_path());
        HttpEntity<MultiValueMap<String, Object>> datas = new HttpEntity<>(form, headers);
        String url = environment.getProperty("my-config.global.python-backend-url") + "/switch_model";
        ResultWebItem recogResult = restTemplate.postForObject(url, datas, ResultWebItem.class);
//        System.out.println(recogResult.getResult());
//        System.out.println(recogResult.getResult().equals("successful"));
        if(recogResult.getResult().equals("successful")) {
//            System.out.println("执行到了这里");
            jdbc.update("UPDATE models SET is_active = 0 WHERE is_active = 1");
            jdbc.update("UPDATE models SET is_active = 1, model_activate_date = ? WHERE model_id = ?",
                    new Date(System.currentTimeMillis()),
                    body.getModel_id()
            );
        }
        return recogResult;
    }

}
