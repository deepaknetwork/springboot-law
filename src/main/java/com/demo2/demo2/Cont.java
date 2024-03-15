package com.demo2.demo2;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
public class Cont {

	@Autowired
	JdbcUserDetailsManager jj;
	
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	DataSource ds;
	@Autowired
	BCryptPasswordEncoder pe;
//	13.137608805299033, 78.1326162841144
//	13.0449408   80.19968
	// 11.1642969  76.9514709
	@PostMapping("/zone")
	String data(@RequestBody loc l) {
		try {
			String aa=String.format("https://overpass-api.de/api/interpreter?data=[out:json];node(around:2000,%.15g,%.15g)[\"amenity\"];out;",l.lat(),l.lon());
			String da=restTemplate.getForObject(aa,String.class);
			 JsonObject jsonObject = new Gson().fromJson(da, JsonObject.class);

		        // Get the "elements" array from the JSON object
		        JsonArray elementsArray = jsonObject.getAsJsonArray("elements");

		        // Create a dynamic array to store amenity values
		        List<String> amenities = new ArrayList<>();

		        // Iterate through the elements array and extract the amenity values
		        for (JsonElement element : elementsArray) {
		            JsonObject elementObject = element.getAsJsonObject();
		            JsonObject tagsObject = elementObject.getAsJsonObject("tags");
		            if (tagsObject.has("amenity")) {
		                String amenity = tagsObject.get("amenity").getAsString();
		                amenities.add(amenity);
		            }
		        }
//
//		        // Print the amenity values
//		        System.out.println("Amenities:");
//		        for (String amenity : amenities) {
//		            System.out.println(amenity);
//		        }
			try {
				return amenities.getFirst().toString();
			}catch(Exception e) {
				return "could not find";
			}
		}catch(Exception e) {
			return e.getMessage().toString();
		}
		
		
	}
	
@PostMapping("/signup")
String signup(@RequestBody UserDet ud) {
	try
	{
		jj.createUser(User.withUsername(ud.name()).password(ud.password()).passwordEncoder(str->pe.encode(str)).roles("USER").build());	
//		JdbcUserDetailsManager j=new JdbcUserDetailsManager(ds);
//		var u1=User.withUsername(ud.name()).password(ud.password()).passwordEncoder(str->pe.encode(str)).roles("USER").build();
//		j.createUser(u1);
		return "signin successfull";
	}catch(Exception e) {
		return e.getMessage().toString();
	}
}


}
