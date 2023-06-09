
package com.btl.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.btl.library.jwt.JwtAuthenFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

	private final JwtAuthenFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.cors().and()
			.csrf().disable()
			.authorizeHttpRequests()
				.requestMatchers("/auth/**")
					.permitAll()
				.requestMatchers("/img/**")
					.permitAll()
				.requestMatchers(HttpMethod.GET, "/book/**")
					.permitAll()
				.requestMatchers(HttpMethod.POST, "/book/**")
					.hasAuthority("ADMIN")
				.requestMatchers(HttpMethod.PUT, "/book/**")
					.hasAuthority("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/book/**")
					.hasAuthority("ADMIN")
				.requestMatchers("/cate/**")
					.permitAll()
				.requestMatchers("/cmt/**", "/order/**")
					.hasAnyAuthority("ADMIN", "USER")
				.anyRequest()
				.authenticated()
			.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
			
		return http.build();
	}
	
}
