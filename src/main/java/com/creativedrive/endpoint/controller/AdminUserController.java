package com.creativedrive.endpoint.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.creativedrive.endpoint.service.UserService;
import com.creativedrive.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Secured("ROLE_ADMIN")
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminUserController 
{
	private final UserService userService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)	
	public ResponseEntity<Iterable<User>> listar()
	{
		return this.buscar(null, 0, 5, "nome");
	}
	
	@PostMapping(path = "/buscar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Iterable<User>> buscar(
    			@RequestBody(required = false) User usr,
	            @RequestParam(defaultValue = "0") Integer page,
	            @RequestParam(defaultValue = "5") Integer size,
	            @RequestParam(defaultValue = "nome") String sort
            ) 
    {   
    	Sort.Order order = new Sort.Order(Direction.ASC, sort);
    	Pageable paging = PageRequest.of(page, size, Sort.by(order));
    	
    	if(usr == null)
    		return new ResponseEntity<>(userService.listar(paging), HttpStatus.OK);    	
    	
        return new ResponseEntity<>(userService.listar(usr, paging), HttpStatus.OK);
    }
    
    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> salvar(@Valid @RequestBody User usr)
    {
    	return new ResponseEntity<>(userService.salvar(usr), HttpStatus.CREATED);
    }
    
    @PutMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> atualizar(@Valid @RequestBody User usr)
    {
    	return new ResponseEntity<>(userService.salvar(usr), HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{idUsr}")
    public ResponseEntity<Void> deletar(@PathVariable String idUsr)
    {
    	try
    	{
    		userService.deletar(idUsr);
    	}
    	catch (Exception e) 
    	{
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
    	
    	return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
