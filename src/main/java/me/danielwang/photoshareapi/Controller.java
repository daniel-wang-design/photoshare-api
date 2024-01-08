package me.danielwang.photoshareapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class Controller {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/api/post")
    public ResponseEntity<?> createUser(@RequestBody UserModel userModel) {
        UserRecord newUser = new UserRecord(userModel.getName());
        UserRecord saved = userRepository.save(newUser);
        return new ResponseEntity(saved, HttpStatus.OK);
    }

    @GetMapping("/api/get")
    public ResponseEntity<?> getUser() {

        List<UserRecord> getUser = userRepository.findAll();

        return new ResponseEntity(getUser, HttpStatus.OK);
    }
}
