package com.fsc.springjwt.payload.response;

import java.util.List;

public class JwtResponse {

	private String acesstoken;
	private String type = "Bearer";
	private String refreshToken;
	private Long id;
	private String username;
	private String email;
	private List<String> pessoas;
	public JwtResponse(String acesstoken, String refreshToken, Long id, String username, String email,
			List<String> pessoas) {
	
		this.acesstoken = acesstoken;
		this.refreshToken = refreshToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.pessoas = pessoas;
	}
	public String getAcesstoken() {
		return acesstoken;
	}
	public void setAcesstoken(String acesstoken) {
		this.acesstoken = acesstoken;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<String> getPessoas() {
		return pessoas;
	}
	public void setPessoas(List<String> pessoas) {
		this.pessoas = pessoas;
	}
	
	
	
}
