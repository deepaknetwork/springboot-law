package com.demo2.demo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

public class YourEntityRepository{

	@Autowired
	RedisTemplate<String, SingleLaw> r;
	
	
}
