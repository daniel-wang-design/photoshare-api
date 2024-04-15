package me.danielwang.photoshareapi.controllers;

import me.danielwang.photoshareapi.Greeting;
import me.danielwang.photoshareapi.dtos.AuthResponse;
import me.danielwang.photoshareapi.dtos.LoginDto;
import me.danielwang.photoshareapi.dtos.SignupDto;
import me.danielwang.photoshareapi.enums.EnumRoles;
import me.danielwang.photoshareapi.models.RoleModel;
import me.danielwang.photoshareapi.models.UserModel;
import me.danielwang.photoshareapi.repositories.RoleRepository;
import me.danielwang.photoshareapi.repositories.UserRepository;
import me.danielwang.photoshareapi.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${earlyAccessToken}")
    private String accessToken;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginDto loginDto) {
        String jwt;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsernameOrEmail(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            jwt = jwtUtils.generateJwtToken(authentication);
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        } // this works
        Optional<UserModel> optionalName = userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail());
        String name;
        if (optionalName.isEmpty()) {
            return new ResponseEntity<>(new AuthResponse("Username not found, please try again later.", null), HttpStatus.BAD_REQUEST);
        }
        name = optionalName.get().getUsername();
        return new ResponseEntity<>(new AuthResponse(jwt, name), HttpStatus.OK);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Greeting adminOnly() {
        return new Greeting(0, "Admin Access Only");
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public Greeting userOnly() {
        return new Greeting(0, "User Access Only");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Greeting allAccess() {
        return new Greeting(0, "All Access");
    }

    public boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[!@#$%^&*()])(?=\\S+$).{8,}$"; // one symbol, number, and at least 8 characters
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @PostMapping("/signup")
    public synchronized ResponseEntity<?> registerUser(@RequestBody SignupDto signupDto) {
        if (signupDto.getUsername().isEmpty()) {
            return new ResponseEntity<>("Please enter a username.", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByUsername(signupDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken, please try enter another", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signupDto.getEmail())) {
            return new ResponseEntity<>("Email already in use, please log in instead.", HttpStatus.BAD_REQUEST);
        }

        if (!isValidPassword(signupDto.getPassword())) {
            return new ResponseEntity<>("Password must be at least 8 characters long and contain a number and symbol.", HttpStatus.BAD_REQUEST);
        }

        if (!Objects.equals(signupDto.getToken(), accessToken)) {
            return new ResponseEntity<>("Invalid access token.", HttpStatus.BAD_REQUEST);
        }

        try {
            UserModel user = new UserModel();
            user.setName(signupDto.getName());
            user.setUsername(signupDto.getUsername());
            user.setEmail(signupDto.getEmail());
            user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

            RoleModel roles = roleRepository.findByName(EnumRoles.ROLE_USER.toString()).get();
            user.setRoles(Collections.singleton(roles));

            userRepository.save(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
}
