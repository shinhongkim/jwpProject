package com.example.jwpproject.controller;

import com.example.jwpproject.model.dto.SignRequest;
import com.example.jwpproject.model.dto.SignResponse;
import com.example.jwpproject.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    private final RedisTemplate redisTemplate;

    @PostMapping(value="/login")
    public ResponseEntity<SignResponse> signIn(@RequestBody SignRequest signRequest) throws Exception {

        SignResponse signResponse = signService.login(signRequest);

       // redisTemplate.opsForValue().set("RT:" + signResponse.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return new ResponseEntity<>(signResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Boolean> signup(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(signService.register(request), HttpStatus.OK);
    }

    @GetMapping("/user/get")
    public ResponseEntity<SignResponse> getUser(@RequestParam("account")  String account) throws Exception {
        return new ResponseEntity<>( signService.getMember(account), HttpStatus.OK);
    }

    @GetMapping("/admin/get")
    public ResponseEntity<SignResponse> getUserForAdmin(@RequestParam String account) throws Exception {
        return new ResponseEntity<>( signService.getMember(account), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public SignResponse logout(
            @RequestBody SignRequest signRequest
    ){
        signService.logout(signRequest.getToken(),signRequest.getRefreshToken());
        SignResponse signResponse = new SignResponse();

        return signResponse;

    }
}
