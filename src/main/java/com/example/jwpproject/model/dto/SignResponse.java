package com.example.jwpproject.model.dto;

import com.example.jwpproject.model.Authority;
import com.example.jwpproject.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {

    private Long id;
    private String account;

    private String nickname;

    private String name;
    private String email;

    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    private String token;

    private String refreshToken;

    public SignResponse(Member member){
        this.id = member.getId();
        this.account = member.getAccount();
        this.nickname = member.getNickname();
        this.name = member.getName();
        this.email = member.getEmail();
        this.roles = member.getRoles();
    }

}
