package com.fsc.springjwt.security.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.fsc.springjwt.model.Usuario;
import com.fsc.springjwt.repository.UsuarioRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Nome de usuário não encontrado: "+
		username));
		return UserDetailsImpl.build(usuario);
	}
	
	
	
}
