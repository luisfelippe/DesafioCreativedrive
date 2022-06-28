package com.creativedrive.security.user;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.creativedrive.model.User;
import com.creativedrive.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsServiceImpl implements UserDetailsService 
{
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) 
    {
        log.info("Procurando no BD o usuário por nome '{}'", username);

        User user = userRepository.findByNome(username);

        log.info("Usuário encontrado '{}'", user);

        if (user == null)
            throw new UsernameNotFoundException(String.format("Usuário '%s' não encontrado", username));

        return new CustomUserDetails(user);
    }

    private static final class CustomUserDetails extends User implements UserDetails 
    {
        CustomUserDetails(@NotNull User applicationUser) 
        {
            super(applicationUser);
        }

        @Override
        public String getPassword() {
            return this.getSenha(); //sobrescreve para pegar o atributo correto do modelo
        }

        @Override
        public String getUsername() {
            return this.getNome(); //sobrescreve para pegar o atributo correto do modelo
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + this.getPerfil());
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
