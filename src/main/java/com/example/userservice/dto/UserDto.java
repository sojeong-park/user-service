package com.example.userservice.dto;

import com.example.userservice.vo.OrderResponse;
import lombok .Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String password;
    private String userId;
    private Date createAt;
    private String encryptedPassword;

    private List<OrderResponse> orders;
}
