package me.danielwang.photoshareapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @JsonProperty("name")
    private String name;
}
