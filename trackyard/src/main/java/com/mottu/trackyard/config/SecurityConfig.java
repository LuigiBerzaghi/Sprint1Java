package com.mottu.trackyard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	  // 1) Usuários em memória (admin/user) com senhas BCRYPT
	  @Bean
	  public UserDetailsService users(PasswordEncoder enc) {
	    UserDetails admin = User.withUsername("admin")
	        .password(enc.encode("admin123"))
	        .authorities("ADMIN") // rótulo simples
	        .build();

	    UserDetails user = User.withUsername("user")
	        .password(enc.encode("user123"))
	        .authorities("USER")
	        .build();

	    return new InMemoryUserDetailsManager(admin, user);
	  }

	  // 2) Encoder
	  @Bean
	  public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	  }

	  // 3) Regras de autorização + formulário de login
	  @Bean
	  public SecurityFilterChain filter(HttpSecurity http) throws Exception {
	    http
	      .authorizeHttpRequests(auth -> auth
	        // recursos públicos
		  .requestMatchers("/css/**","/js/**","/images/**","/login","/error","/acesso-negado").permitAll()


	        // leitura (GET) liberada para USER e ADMIN (authenticados)
	        .requestMatchers(HttpMethod.GET, "/**").hasAnyAuthority("USER","ADMIN")

	        // escrita (POST/PUT/DELETE) só ADMIN
	        .requestMatchers(HttpMethod.POST, "/**").hasAuthority("ADMIN")
	        .requestMatchers(HttpMethod.PUT, "/**").hasAuthority("ADMIN")
	        .requestMatchers(HttpMethod.DELETE, "/**").hasAuthority("ADMIN")

	        // qualquer outra rota exige login
	        .anyRequest().authenticated()
	      )
	      .formLogin(form -> form
	        .loginPage("/login")               
	        .defaultSuccessUrl("/motos", true)      
	        .permitAll()
	      )
	      .logout(l -> l.logoutUrl("/logout")
	        .logoutSuccessUrl("/login?logout")
	        .permitAll()
	      )
	    // Se for usar H2-console em dev, descomenta:
	    .headers(h -> h.frameOptions(o -> o.disable()))
	    .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
	    .exceptionHandling(e -> e.accessDeniedHandler((req,res,ex) ->
	      res.sendRedirect(req.getContextPath() + "/acesso-negado")
	  ));


	    return http.build();
	  }
	
}
