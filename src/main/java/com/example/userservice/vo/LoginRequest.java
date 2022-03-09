package com.example.userservice.vo;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {
    @NotNull(message = "email is not null.")
    @Size(min=2, message = "이메일은 2글자 이상 작성하여야 합니다.")
    @Email
    private String email;

    @NotNull(message = "password is not null.")
    @Size(min=8, message="비밀번호는 8글자 이상 작성하여야 합니다.")
    private String password;
}
