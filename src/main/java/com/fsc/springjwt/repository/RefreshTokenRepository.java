package com.fsc.springjwt.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.fsc.springjwt.model.RefreshToken;
import com.fsc.springjwt.model.Usuario;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	  Optional<RefreshToken> findByToken(String token);

	  @Modifying
	  int deleteByUser(Usuario usuario);
	}
