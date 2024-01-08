package me.danielwang.photoshareapi;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PhotoshareApiApplication {

    @Autowired
    private PasswordLoader passwordLoader;

    public static void main(String[] args) {
        SpringApplication.run(PhotoshareApiApplication.class, args);
    }

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;
}
