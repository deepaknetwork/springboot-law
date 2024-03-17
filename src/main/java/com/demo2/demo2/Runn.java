package com.demo2.demo2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Component
public class Runn implements CommandLineRunner {

//	@Autowired
//	LasRepository l;
//	
	@Autowired
	RedisTemplate<String,List<Laws>> r;
	
	@Override
	public void run(String... args) throws Exception {
	
		// Serialize the list of SingleLaw objects to JSON
//		ObjectMapper objectMapper = new ObjectMapper();
//		Laws json = objectMapper.writeValueAsString(List.of(
//		        new SingleLaw("ACT13-2004", "students", "okay"),
//		        new SingleLaw("ACT14-2005", "public", "done")
//		));
//
//		// Store the JSON string in Redis
//		r.opsForValue().set("b", json);
//
//		// Retrieve the JSON string from Redis
//		Laws storedJson = r.opsForValue().get("b");
//
//		// Deserialize the JSON string from Redis
//		List<SingleLaw> singleLawList = objectMapper.readValue(storedJson, new TypeReference<List<SingleLaw>>() {});
//
//		System.out.println(singleLawList);

//r.opsForValue().set("a", List.of(new SingleLaw("ACT13-2004","students","okay"),
//		new SingleLaw("ACT14-2005","publc","done")));
//System.out.println(r.opsForValue().get("a"));
//		
//		
//		Laws l1=new Laws("ACT 32-2004","people sre always welcomed to ge appreciated","SCHOOL");
//	l.save(l1);
//	Laws l2=new Laws("ACT 43-2004","im soo cool, but peoples can make it fast","PUBLIC");
//	l.save(l2);
	}

}
