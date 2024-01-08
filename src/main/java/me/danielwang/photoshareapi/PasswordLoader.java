package me.danielwang.photoshareapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:passwords.env")
public class PasswordLoader {

    @Value("${postgrespwd}")
    private String dbPassword;

    public String getDbPassword() {
        return dbPassword;
    }
}
