package com.fsc.springjwt.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fsc.springjwt.security.services.UserDetailsServiceImpl;

public class AuthTokenFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	 UserDetailsService userDetailsService;
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = parseJwt(request);
			if(jwt != null && jwtUtils.validateJwtToken(jwt)) {
				String username = jwtUtils.getUserNameFromJwtToken(jwt);
				
	UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authenticationToken = new
						UsernamePasswordAuthenticationToken(userDetails,
								userDetails.getAuthorities());
				
				authenticationToken.setDetails(new WebAuthenticationDetailsSource()
						.buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
			
		} catch (Exception e) {
			LOGGER.error("Não é possível definir a autenticação do usuário {}", e.getMessage());;
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		
		if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}
		
		return null;
	}
	
	  
}
