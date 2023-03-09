package com.example.jwpproject.controller;

import com.example.jwpproject.model.dto.SignRequest;
import com.example.jwpproject.model.dto.SignResponse;
import com.example.jwpproject.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @PostMapping(value="/login")
    public ResponseEntity<SignResponse> signIn(@RequestBody SignRequest signRequest) throws Exception {
        return new ResponseEntity<>(signService.login(signRequest), HttpStatus.OK);
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

}
