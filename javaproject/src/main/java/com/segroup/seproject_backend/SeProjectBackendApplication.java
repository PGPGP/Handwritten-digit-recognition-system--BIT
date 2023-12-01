package com.segroup.seproject_backend;

import com.segroup.seproject_backend.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class SeProjectBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeProjectBackendApplication.class, args);
    }

}
