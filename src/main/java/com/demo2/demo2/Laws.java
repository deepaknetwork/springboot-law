package com.demo2.demo2;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


public class Laws implements Serializable{


public String id;

public String name;

public String description;

public String zone;

Laws() {
	
}

public Laws(String name, String description, String zone) {
	super();
	this.name = name;
	this.description = description;
	this.zone = zone;
}


}
