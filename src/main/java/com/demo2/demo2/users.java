package com.demo2.demo2;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//@Entity
public class users {

	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    public Long id;

	    public String username;
	    
	    public String password;
	    
	    public String roles;
	    
	    public boolean enabled;
}
