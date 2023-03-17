package com.example.jwpproject.repository;

import com.example.jwpproject.model.dto.RedisUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRedisRepository extends CrudRepository<RedisUser ,Long> {
}
