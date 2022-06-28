package com.creativedrive.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.filter.OncePerRequestFilter;

import com.creativedrive.repository.UserRepository;
import com.creativedrive.security.property.JwtConfiguration;
import com.creativedrive.security.service.JWTTokenAutenticacaoService;
import com.creativedrive.security.token.converter.TokenConverter;
import com.creativedrive.security.token.creator.TokenCreator;
import com.creativedrive.security.token.util.SecurityContextUtil;
import com.nimbusds.jwt.SignedJWT;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

//filtro onde todas as requisições serão capturadas para autenticar
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtApiAutenticacaoFilter extends OncePerRequestFilter 
{
    protected final JwtConfiguration jwtConfiguration;	
    protected final TokenConverter tokenConverter;

    @Override
    @SuppressWarnings("Duplicates")
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException 
    {
        String header = request.getHeader(jwtConfiguration.getHeader_name());

        if (header == null || !header.startsWith(jwtConfiguration.getPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(jwtConfiguration.getPrefix(), "").trim();

        SecurityContextUtil.setSecurityContext(StringUtils.equalsIgnoreCase("signed", jwtConfiguration.getType()) ? validate(token) : decryptValidating(token));

        JWTTokenAutenticacaoService.liberacaoCors(response);
        chain.doFilter(request, response);
    }

    @SneakyThrows
    private SignedJWT decryptValidating(String encryptedToken) 
    {
        String signedToken = tokenConverter.decryptToken(encryptedToken);
        tokenConverter.validateTokenSignature(signedToken);
        return SignedJWT.parse(signedToken);
    }

    @SneakyThrows
    private SignedJWT validate(String signedToken) 
    {
        tokenConverter.validateTokenSignature(signedToken);
        return SignedJWT.parse(signedToken);
    }
}
