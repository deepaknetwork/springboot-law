package com.demo2.demo2;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
public class Controller {

	@Autowired
	UserDetailsService userdetailsservice; 	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	BCryptPasswordEncoder pe;
	@Autowired
	RedisTemplate<String, List<Laws>> r;
	
	@GetMapping("/all")
	List<List<Laws>> laws() {
		return r.opsForValue().multiGet(r.keys("*"));
				 
	}
	@DeleteMapping("/deleteall")
	String dltall() {
		r.delete(r.keys("*"));
		return "deleted";
	}
	
	@PutMapping("/delete")
	String dltone(@RequestBody Laws laws) {
		List<Laws> la=r.opsForValue().get(laws.zone);
		la.removeIf(item -> item.id.equals(laws.id));
		 r.opsForValue().set(laws.zone, la);
		return "deleted";
	}
	
	@PostMapping("/add")
	String adda(@RequestBody Laws laws) {
		if(r.opsForValue().get(laws.zone)!=null) {
			Object value = r.opsForValue().get(laws.zone);
		    if (value instanceof List) {
		    	 List<Laws> la = new ArrayList<>((List<Laws>) value);
		         la.add(laws);
		         r.opsForValue().set(laws.zone, la);
		         return "added";
		    } else {
		        return "errer";
		    }
		}else {
			r.opsForValue().set(laws.zone,List.of(laws));
			return "added";
		}
		
	}
	@PostMapping("/add-all")
	String addall(@RequestBody List<Laws> lawss) {
		try {
			for(Laws laws:lawss) {
				if(r.opsForValue().get(laws.zone)!=null) {
					Object value = r.opsForValue().get(laws.zone);
				    if (value instanceof List) {
				    	 List<Laws> la = new ArrayList<>((List<Laws>) value);
				         la.add(laws);
				         r.opsForValue().set(laws.zone, la);
				    } else {
				    }
				}else {
					r.opsForValue().set(laws.zone,List.of(laws));
				}
			}
			return "added";
			
		}catch(Exception e) {
			return e.getMessage();
		}
		
		
	}
	
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
			try {
				return amenities.getFirst().toString();
			}catch(Exception e) {
				return "could not find";
			}
		}catch(Exception e) {
			return e.getMessage().toString();
		}
		
		
	}
	
@PostMapping("/user")
String signup(@RequestBody UserDet ud) {
	try
	{
		JdbcUserDetailsManager userlist=(JdbcUserDetailsManager)userdetailsservice;
		if(userlist.userExists(ud.name())) {
			return "please use another username";
		}else {
			userlist.createUser(User.withUsername(ud.name()).password(ud.password()).passwordEncoder(str->pe.encode(str)).roles("USER").build());
			return "user signed up";
		}
	}catch(Exception e) {
		return e.getMessage().toString();
	}
}
@DeleteMapping("/user/{name}")
String deleteuser(@PathVariable String name) {
	
	try
	{
		JdbcUserDetailsManager userlist=(JdbcUserDetailsManager)userdetailsservice;
		if(userlist.userExists(name)) {
			userlist.deleteUser(name);
			return "user deleted";
		}else {
			return "user dont exist";
		}
	}catch(Exception e) {
		return e.getMessage().toString();
	}
}

@DeleteMapping("/users")
String deletealluser() {
	try
	{
		JdbcUserDetailsManager userlist=(JdbcUserDetailsManager)userdetailsservice;
		String query = " SELECT username FROM authorities WHERE authority != 'ROLE_ADMIN'";

		// Execute query and get usernames
		List<String> usernamesToDelete = userlist.getJdbcTemplate().queryForList(query, String.class);
		for (String username:usernamesToDelete) {
			    userlist.deleteUser(username);
			  } 
		return "all deleted";

	}catch(Exception e) {
		return e.getMessage();
	}
}

@GetMapping("/user")
List<String> alluser() {
	
		JdbcUserDetailsManager userlist=(JdbcUserDetailsManager)userdetailsservice;
		String query = "SELECT username FROM users";
		// Execute query and get usernames
		List<String> usernames = userlist.getJdbcTemplate().queryForList(query, String.class);
		
		return usernames;

}


}
