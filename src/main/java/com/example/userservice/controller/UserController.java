package com.example.userservice.controller;

import com.example.userservice.domain.UserEntity;
import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.UserRequest;
import com.example.userservice.vo.UserResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {
    private Environment env;
    private UserService userService;

    @Autowired
    public UserController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
    }

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's working in User service"
                + ", port (local.server.port) =" + env.getProperty("local.server.port")
                + ", token secret =" + env.getProperty("token.secret")
                + ", token expiration time =" + env.getProperty("token.expiration_time"));
    }

    @GetMapping("/welcome")
    public String welcome() {
        return env.getProperty("greeting.message");
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody UserRequest userRequest) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(userRequest, UserDto.class);

        UserResponse userResponse = mapper.map(userService.createUser(userDto), UserResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity getUser(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserByUserId(userId);
        UserResponse userResponse = new ModelMapper().map(userDto, UserResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUserList() {
        Iterable<UserEntity> users = userService.getUserByAll();

        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach(v -> {
            userResponses.add(new ModelMapper().map(v, UserResponse.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(userResponses);
    }
}
