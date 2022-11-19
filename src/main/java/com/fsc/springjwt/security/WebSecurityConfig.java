package com.fsc.springjwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fsc.springjwt.security.jwt.AuthEntryPointJwt;
import com.fsc.springjwt.security.jwt.AuthTokenFilter;
import com.fsc.springjwt.security.services.UserDetailsServiceImpl;

@Configuration
@EnableGlobalMethodSecurity(
	//	securedEnabled = true,
	//	 jsr250Enabled = true,
		prePostEnabled = true)

public class WebSecurityConfig {

	@Autowired(required = true)
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private AuthEntryPointJwt authEntryPointJwt;
	
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Bean
	public PasswordEncoder passworEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() throws Exception {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	
		authProvider.setUserDetailsService(userDetailsServiceImpl);
		authProvider.setPasswordEncoder(passworEncoder());
		
		return authProvider;
	
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
	throws Exception{
		return authConfig.getAuthenticationManager();
	}
	
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http.cors().and().csrf().disable()
		.exceptionHandling().authenticationEntryPoint(authEntryPointJwt).and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests().antMatchers("/api/auth/**").permitAll()
		.antMatchers("/api/test/**").permitAll()
		.anyRequest().authenticated();
		
		http.authenticationProvider(authenticationProvider());
		
		http.addFilterBefore(authenticationJwtTokenFilter(),
				UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
}
