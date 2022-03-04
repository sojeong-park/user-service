package com.example.userservice.domain;

import com.example.userservice.dto.UserDto;
import lombok.Data;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.util.UUID;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String encryptedPassword;

    public User(UserDto userDto) {
        this.email = userDto.getEmail();
        this.name = userDto.getName();
        this.userId = UUID.randomUUID().toString();
        this.encryptedPassword = "encryptedPassword";
    }
}
