package com.example.userservice.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserRequest {
    @NotNull(message = "Email is not null")
    @Size(min = 5, message = "Email not be less than 5 characters")
    @Email
    private String email;

    @NotNull(message = "Id is not null")
    @Size(min = 2, message = "Id not be less than 2 characters")
    private String id;

    @NotNull(message = "Password is not null")
    @Size(min = 8, message = "Password not be less than 8 characters")
    private String password;
}
