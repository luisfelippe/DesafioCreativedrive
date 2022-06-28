package com.creativedrive.security.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import com.creativedrive.security.filter.JWTLoginFilter;
import com.creativedrive.security.filter.JwtApiAutenticacaoFilter;
import com.creativedrive.security.property.JwtConfiguration;
import com.creativedrive.security.token.converter.TokenConverter;
import com.creativedrive.security.token.creator.TokenCreator;
import com.creativedrive.util.ApplicationContextLoad;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebConfigSecurity extends WebSecurityConfigurerAdapter 
{
	protected final JwtConfiguration jwtConfiguration;
	protected final UserDetailsService userDetailsService;
    protected final TokenCreator tokenCreator;
    protected final TokenConverter tokenConverter; 

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {    	
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
//		http
//			.csrf()
//			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//			.disable()
//			.authorizeRequests() //libera os acessos aos endpoints que serão públicos
//				.antMatchers("/").permitAll()
//				.antMatchers("/login").permitAll()
//				.antMatchers("/index").permitAll()
//				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//				
//				//redireciona ou da retorno ao index se sair do sistema
//				.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
//				
//				//mapeia o logout
//				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//				
//				//filtra as requisições com login jwt
//				.and().addFilterAfter(ApplicationContextLoad.getApplicationContext().getBean(JWTLoginFilter.class), UsernamePasswordAuthenticationFilter.class)
//				
//				.addFilterBefore(ApplicationContextLoad.getApplicationContext().getBean(JwtApiAutenticacaoFilter.class), UsernamePasswordAuthenticationFilter.class);
		
		http
		.csrf().disable()
        .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling().authenticationEntryPoint((req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        .and()
        .authorizeRequests()
        .antMatchers(jwtConfiguration.getUrl(), "/**/swagger-ui.html").permitAll()
        //.antMatchers(HttpMethod.GET, "/**/swagger-resources/**", "/**/webjars/springfox-swagger-ui/**", "/**/v2/api-docs/**").permitAll()
        .antMatchers("/admin/**").hasRole("ADMIN")
        //.antMatchers("/auth/user/**").hasAnyRole("ADMIN", "USER")
        .anyRequest().authenticated()
        .and()
        .addFilter(new JWTLoginFilter(authenticationManager(), jwtConfiguration, tokenCreator))
        .addFilterAfter(new JwtApiAutenticacaoFilter(jwtConfiguration, tokenConverter), UsernamePasswordAuthenticationFilter.class);;
	}    
}
