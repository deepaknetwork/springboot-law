package com.demo2.demo2;

import java.io.Serializable;

public class SingleLaw implements Serializable {

     public String id;
     public String title;
     public String discription;
     public SingleLaw() {
     }

	public SingleLaw(String id, String title, String discription) {
		super();
		this.id = id;
		this.title = title;
		this.discription = discription;
	}
	

}
