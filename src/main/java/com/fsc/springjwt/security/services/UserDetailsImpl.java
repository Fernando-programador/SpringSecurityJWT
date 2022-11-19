package com.fsc.springjwt.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsc.springjwt.model.Usuario;


public class UserDetailsImpl implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String username;
	private String email;
	
	@JsonIgnore
	private String password;
	
	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Long id, String username, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		
	}

	public UserDetailsImpl() {
	
	}

	public static UserDetailsImpl build(Usuario usuario) {
		List<GrantedAuthority> authorities = usuario.getPessoas().stream()
				.map(pessoa -> new SimpleGrantedAuthority(pessoa.getNome().name()))
				.collect(Collectors.toList());
		
		return new UserDetailsImpl(
				usuario.getId(),
				usuario.getUsername(),
				usuario.getEmail(),
				usuario.getPassword(),
				authorities);
	}
	//daqui para baixo foram todos importados 

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	
		return authorities;
	}

	
	public Long getId() {
		
		return id;
	}

	
	public String getEmail() {
		
		return email;
	}
	
	public String getPassword() {
		
		return password;
	}
	
	public String getUsername() {
		
		return username;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDetailsImpl usuario = (UserDetailsImpl) obj;
		return Objects.equals(id, usuario.id);
	}
	
	


}
