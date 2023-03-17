package com.example.jwpproject.service;

import com.example.jwpproject.model.dto.RedisUser;
import com.example.jwpproject.repository.UserRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HelthCheckService {

    private final UserRedisRepository userRedisRepository;

    public void createUser(){
        RedisUser user = new RedisUser(1L,"hide");
        userRedisRepository.save(user);
    }

    public void getUser() {
        Optional<RedisUser> user = userRedisRepository.findById(1L);
        System.out.println(user.get());
    }
}
