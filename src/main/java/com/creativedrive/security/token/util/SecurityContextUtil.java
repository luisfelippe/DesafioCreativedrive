package com.creativedrive.security.token.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.creativedrive.model.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityContextUtil 
{
    private SecurityContextUtil() 
    {

    }

    public static void setSecurityContext(SignedJWT signedJWT) 
    {
        try
        {
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            String username = claims.getSubject();
            if (username == null)
                throw new JOSEException("Username missing from JWT");

            List<String> authorities = claims.getStringListClaim("authorities");
            User applicationUser = User
                    .builder()
                    .id(claims.getStringClaim("userId"))
                    .nome(username)
                    .perfil(String.join(",", authorities))
                    .build();
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(applicationUser, null, createAuthorities(authorities));
            auth.setDetails(signedJWT.serialize());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        catch (Exception e) 
        {
            log.error("Erro ao setar ao contexto de segurança ", e);
            SecurityContextHolder.clearContext();
        }
    }

    private static List<SimpleGrantedAuthority> createAuthorities(List<String> authorities) 
    {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
