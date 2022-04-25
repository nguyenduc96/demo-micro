package com.example.authserver.sercurity;

import com.example.authserver.service.JwtService;
import com.example.authserver.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = getJwtTokenFromRequest(request);
			if (token != null && jwtService.validateJwtToken(token)) {
				String username = jwtService.getUserNameFromJwtToken(token);
				UserDetails userDetails = userService.loadUserByUsername(username);

				UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		} catch (ExpiredJwtException ex) {
			String isRefresh = request.getHeader("isRefreshToken");
			String requestURL = request.getRequestURL().toString();
			if ("true".equals(isRefresh) && requestURL.contains("refreshtoken")) {
				allowRefreshToken(ex, request);
			} else
				request.setAttribute("exception", ex);
		} catch (BadCredentialsException ex) {
			request.setAttribute("exception", ex);
		} catch (Exception e) {
			logger.error("Authentication fail: {}", e);
			throw new RuntimeException("sai token");
		}
		filterChain.doFilter(request, response);
	}

	private void allowRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(null, null, null);

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		request.setAttribute("claims", ex.getClaims());
	}

	private String getJwtTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
