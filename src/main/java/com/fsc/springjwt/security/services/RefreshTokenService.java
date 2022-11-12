package com.fsc.springjwt.security.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fsc.springjwt.exception.TokenRefreshException;
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
	
	public RefreshToken verifyExpiration(RefreshToken token) {
		if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(), "O token de atualização expirou."
					+ " Por favor, faça uma nova solicitação de sinalização");
		}
		return token;
	}
	
	
	@Transactional
	public int deleteByUsuarioiId(Long usuario_id) {
		return refreshTokenRepository.deleteByUser(usuarioRepository
				.findById(usuario_id).get());
	}
	
	
}
