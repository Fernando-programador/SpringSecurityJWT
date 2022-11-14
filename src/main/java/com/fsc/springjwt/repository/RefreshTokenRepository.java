package com.fsc.springjwt.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


import com.fsc.springjwt.model.RefreshToken;
import com.fsc.springjwt.model.Usuario;


@Repository
@EnableJpaRepositories
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	  Optional<RefreshToken> findByToken(String token);

	  @Modifying
	  int findByUsuario(Usuario usuario);
	}
