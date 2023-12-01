package com.segroup.seproject_backend.controller;

import com.segroup.seproject_backend.data_item.TestArrayItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class TestController {

    @ResponseBody
    @PostMapping("/test_array")
    public String test_array(TestArrayItem body) {
        for (int i = 0; i < body.getArray().size(); i++) {
            System.out.println(body.getArray().get(i));
        }
        return "Hello";
    }
}
