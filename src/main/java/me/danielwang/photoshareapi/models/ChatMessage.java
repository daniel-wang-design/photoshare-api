package me.danielwang.photoshareapi.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString

public class ChatMessage {
    private String nickname;
    private String content;
}