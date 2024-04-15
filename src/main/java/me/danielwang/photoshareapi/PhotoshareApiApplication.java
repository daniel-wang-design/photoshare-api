package me.danielwang.photoshareapi;

import me.danielwang.photoshareapi.config.PasswordLoader;
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

    @Value("${spring.datasource.url}")
    private String dataSourceLocation;

    @Value("${spring.datasource.username}")
    private String dataSourceUser;

    @Value("${jwtExpirationMs}")
    private String jwtExpirationMs;

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${accessToken}")
    private String accessToken;
}
