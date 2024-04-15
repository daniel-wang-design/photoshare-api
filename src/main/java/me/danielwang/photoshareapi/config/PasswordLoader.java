package me.danielwang.photoshareapi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource("classpath:passwords.env")
public class PasswordLoader {

    @Value("${postgrespwd}")
    private String dbPassword;

    @Value("${postgresdb}")
    private String dblocation;

    @Value("${jwtExpirationMs}")
    private String jwtExpirationMs;

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${earlyAccessToken}")
    private String accessToken;
}
