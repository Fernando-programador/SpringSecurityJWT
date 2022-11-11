package com.fsc.springjwt.security.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fsc.springjwt.model.RefreshToken;
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
	
	public Optional<RefreshToken> findByToken(String token){
		return refreshTokenRepository.findByToken(token);
	}
	
	public RefreshToken createRefreshToken(Long usuario_Id) {
		RefreshToken refreshToken = new RefreshToken();
		
		refreshToken.setUsuario(usuarioRepository.findById(usuario_Id).get());
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());
		
		refreshToken = refreshTokenRepository.save(refreshToken);
		
		return refreshToken;
	}
	
	
	
	
	
}
