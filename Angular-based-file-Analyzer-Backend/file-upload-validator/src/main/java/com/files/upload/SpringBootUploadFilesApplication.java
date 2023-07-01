package com.files.upload;

// import javax.annotation.Resource; // for Spring Boot 2

import jakarta.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootUploadFilesApplication {


    public static void main(String[] args) {

        SpringApplication.run(SpringBootUploadFilesApplication.class, args);
    }
}
