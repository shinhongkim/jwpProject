package com.example.jwpproject.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignRequest {

    private Long id;
    private String account;

    private String password;

    private String nickname;

    private String name;
    private String email;


    private String token;

    private String refreshToken;
}
