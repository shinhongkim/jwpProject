package com.example.jwpproject.model.dto;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value="redisUser")
public class RedisUser {

    @Id
    private Long id;
    private String name;

    public RedisUser(Long id,String name){
        this.id= id;
        this.name=name;
    }
}
