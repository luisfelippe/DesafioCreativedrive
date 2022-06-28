package com.creativedrive.endpoint.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.creativedrive.model.User;
import com.creativedrive.repository.UserCustomRepository;
import com.creativedrive.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService 
{
	private final UserRepository userRepository;	
	private final UserCustomRepository customRepository;

    public Iterable<User> listar(Pageable pageable)
    {
        log.info("Listando todos os usuários");

        return userRepository.findAll(pageable);
    }
    
    public Iterable<User> listar(User usr, Pageable pageable)
    {
    	return customRepository.buscar(usr, pageable);
    }
    
    public User salvar(User usr)
    {
    	log.info("Validando o usuário para a persistência!");
    	
    	//verifica se é um usuário novo e codifica a senha os demais campos são validados pelo validation
    	if(usr != null && (usr.getId() == null || usr.getId().trim().isEmpty()))
    		usr.setSenha(new BCryptPasswordEncoder().encode(usr.getSenha()));
    	
    	log.info("Persistindo o usuário!");
    	
    	return userRepository.save(usr);
    }
    
    public void deletar(String id) throws Exception
    {
    	User usr = userRepository.findById(id).get();
    	
    	if(usr == null)
    		throw new Exception("Usuário não encontrado!");
    	
    	userRepository.delete(usr);
    }
}
