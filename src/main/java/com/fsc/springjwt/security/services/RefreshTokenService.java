package com.fsc.springjwt.security.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fsc.springjwt.repository.RefreshTokenRepository;
import com.fsc.springjwt.repository.UsuarioRepository;

@Service
public class RefreshTokenService {

	@Value("${fsc.app.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Optional<T>
	
}
