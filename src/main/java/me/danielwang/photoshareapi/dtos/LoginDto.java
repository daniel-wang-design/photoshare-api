package me.danielwang.photoshareapi.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String usernameOrEmail;
    private String password;
}