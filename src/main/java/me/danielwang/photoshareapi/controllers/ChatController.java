package me.danielwang.photoshareapi.controllers;

import me.danielwang.photoshareapi.dtos.ConnectedUsers;
import me.danielwang.photoshareapi.models.ChatMessage;
import me.danielwang.photoshareapi.models.UserModel;
import me.danielwang.photoshareapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Controller
@EnableScheduling
public class ChatController {
    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @Scheduled(fixedRate = 1000)
    public void getConnectedClients() {
        Set<SimpUser> users = simpUserRegistry.getUsers();
        ConnectedUsers connectedUsers = new ConnectedUsers();
        connectedUsers.setUsernames(new ArrayList<>());
        for (SimpUser user : users) {
            String username = user.getName();
            Optional<UserModel> name = userRepository.findByUsernameOrEmail(username, username);
            if (name.isPresent()) {
                username = name.get().getUsername();
            }
            connectedUsers.getUsernames().add(username);
        }
        connectedUsers.setCount(users.size());
        messagingTemplate.convertAndSend("/topic/users", connectedUsers);
    }
}