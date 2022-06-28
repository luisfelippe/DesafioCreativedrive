package com.creativedrive.security.filter;

import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.creativedrive.model.User;
import com.creativedrive.repository.UserRepository;
import com.creativedrive.security.property.JwtConfiguration;
import com.creativedrive.security.service.JWTTokenAutenticacaoService;
import com.creativedrive.security.token.creator.TokenCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter
{    
	private final AuthenticationManager authenticationManager;
    private final JwtConfiguration jwtConfiguration;	
    private final TokenCreator tokenCreator;
    
    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
    {    	
        log.info("Autenticando. . .");
        User applicationUser = new ObjectMapper().readValue(request.getInputStream(), User.class);

        if (applicationUser == null)
            throw new UsernameNotFoundException("Incapaz de recuperar username or password");

        log.info("Criando objeto de autenticação para o usuário '{}' e chamando UserDetailServiceImpl loadUserByUsername", applicationUser.getNome());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(applicationUser.getNome(), applicationUser.getSenha(), Collections.emptyList());

        usernamePasswordAuthenticationToken.setDetails(applicationUser);

        //JWTTokenAutenticacaoService.liberacaoCors(response);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    @SneakyThrows
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) 
    {
        log.info("Usuário '{}' autenticado com sucesso, gerando JWE token", auth.getName());

        SignedJWT signedJWT = tokenCreator.createSignedJWT(auth);

        String encryptedToken = tokenCreator.encryptToken(signedJWT);

        log.info("Token gerado com sucesso, adicionando ao response header");

        response.addHeader("Access-Control-Expose-Headers", "XSRF-TOKEN, " + jwtConfiguration.getHeader_name());

        response.addHeader(jwtConfiguration.getHeader_name(), jwtConfiguration.getPrefix() + encryptedToken);
        
        response.getWriter().write("{\"" + jwtConfiguration.getPrefix() + "\":\"" + encryptedToken + "\"}");
        
        JWTTokenAutenticacaoService.liberacaoCors(response);
    }

}
