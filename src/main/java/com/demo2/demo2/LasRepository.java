package com.demo2.demo2;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LasRepository extends JpaRepository<Laws, Long>  {

	public List<Laws> findByZone(String zone);
	}

