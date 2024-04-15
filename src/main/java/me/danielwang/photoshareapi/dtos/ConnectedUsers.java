package me.danielwang.photoshareapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectedUsers {
    ArrayList<String> usernames;
    int count;
}
