package com.segroup.seproject_backend.controller;

import com.segroup.seproject_backend.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class TestRepoController {

    @Autowired
    private ProjectRepo projectRepo;


    @RequestMapping("/test_repo")
    @ResponseBody
    public String testRepo(){
        Date now = new Date();
        projectRepo.recordOneUse(now, 0);
        projectRepo.recordOneRightFeedback(now, 0);
        projectRepo.recordOneWrongFeedback(now, 1);
        return "Hello world!\n";
    }
}
