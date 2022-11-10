package com.fsc.springjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fsc.springjwt.model.Enunpessoa;
import com.fsc.springjwt.model.Pessoa;


@Repository
public interface RefreshTokenRepository extends JpaRepository<Pessoa, Long> {

	Optional<Pessoa> findByName(Enunpessoa nome);

	
	
	
}
