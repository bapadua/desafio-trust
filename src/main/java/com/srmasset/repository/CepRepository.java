package com.srmasset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.srmasset.entity.CepRequest;

@Repository
public interface CepRepository extends JpaRepository<CepRequest, Long>{

	@Query("select c from CepRequest c")
	List<CepRequest> getAll();
}
