package com.fsc.springjwt.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fsc.springjwt.exception.TokenRefreshException;
import com.fsc.springjwt.model.EnunPessoa;
import com.fsc.springjwt.model.Pessoa;
import com.fsc.springjwt.model.RefreshToken;
import com.fsc.springjwt.model.Usuario;
import com.fsc.springjwt.payload.request.LoginRequest;
import com.fsc.springjwt.payload.request.SignupRequest;
import com.fsc.springjwt.payload.request.TokenRefreshRequest;
import com.fsc.springjwt.payload.response.JwtResponse;
import com.fsc.springjwt.payload.response.MessageResponse;
import com.fsc.springjwt.payload.response.TokenRefreshResponse;
import com.fsc.springjwt.repository.PessoaRepository;
import com.fsc.springjwt.repository.UsuarioRepository;
import com.fsc.springjwt.security.jwt.JwtUtils;
import com.fsc.springjwt.security.services.RefreshTokenService;
import com.fsc.springjwt.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private RefreshTokenService refreshTokenService;

	
	@PostMapping("/atualizartoken") //requesttoken
	public ResponseEntity<?> atualizarToken(@Valid @RequestBody TokenRefreshRequest request){
		String requestRefreshToken = request.getRefreshToken();
		
		return refreshTokenService.findByToken(requestRefreshToken)
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUsuario)
				.map(usuario -> {
					String token =jwtUtils.generateTokenFromUsername(usuario.getUsername());
					return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
				})
				
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database"));
	}
	
			
	@PostMapping("/sair") //signout
	public ResponseEntity<?> usuarioSaindo(){
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal();
		Long usuarioId = userDetailsImpl.getId();
		refreshTokenService.deleteByUsuario(usuarioId);
		return ResponseEntity.ok(new MessageResponse("Você não está mais logado."));
	
	}
	
	
	
	@PostMapping//("/signup") // signup cadastrar
	public ResponseEntity<?> registrarUsuario(@Valid @RequestBody SignupRequest signUpRequest) {
		if (usuarioRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Erro:Nome de usuário" + signUpRequest.getUsername() + "já cadastrado"));
		}
		
		if(usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Erro: Email " + signUpRequest.getEmail() + " já cadastrado."));
		}

		
		
		//criar usuario
		Usuario usuario = new Usuario(signUpRequest.getUsername(), signUpRequest.getEmail(),
				passwordEncoder.encode(signUpRequest.getPassword()));

		Set<String> criaPessoa = signUpRequest.getPessoa();
		Set<Pessoa> pessoas = new HashSet<>();

		
		if (criaPessoa == null) {
			Pessoa usuarioPessoa = pessoaRepository.findByNome(EnunPessoa.PESSOA_USER)
					.orElseThrow(() -> new RuntimeException("Erro: Pessoa já exixtente."));
			pessoas.add(usuarioPessoa);
		} else {
			criaPessoa.forEach(pessoa -> {
				switch (pessoa) {
				case "admin":
					Pessoa adminPessoa = pessoaRepository.findByNome(EnunPessoa.PESSOA_ADMIN)
							.orElseThrow(() -> new RuntimeException("Erro: Pessoa já existente. "));
					pessoas.add(adminPessoa);
					break;

				case "mod":
					Pessoa modPessoa = pessoaRepository.findByNome(EnunPessoa.PESSOA_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Erro: Pessoa já existente. "));
					pessoas.add(modPessoa);
					break;

				default:
					Pessoa usuarioPessoa = pessoaRepository.findByNome(EnunPessoa.PESSOA_USER)
							.orElseThrow(() -> new RuntimeException("Erro: Pessoa já existente. "));
					pessoas.add(usuarioPessoa);
				}
			});
		}	
		usuario.setPessoas(pessoas);
		usuarioRepository.save(usuario);
		
		return ResponseEntity.ok(new MessageResponse("Usuário registrado com Sucesso!!!"));		


	}
	
	@PostMapping("/entrar") // signin
	public ResponseEntity<?> autenticarUsuario(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

		String jwt = jwtUtils.generateJwtToken(userDetailsImpl);

		List<String> pessoas = userDetailsImpl.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetailsImpl.getId());

		return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetailsImpl.getId(),
				userDetailsImpl.getUsername(), userDetailsImpl.getEmail(), pessoas));
		
		
	}

	



}


	


