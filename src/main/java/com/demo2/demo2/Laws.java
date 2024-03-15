package com.demo2.demo2;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Laws {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
public Long id;

public String name;

public String discription;
}
