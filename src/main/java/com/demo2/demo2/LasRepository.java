package com.demo2.demo2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LasRepository extends JpaRepository<Laws, Long>  {

}
