package com.creativedrive.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.MongoRegexCreator.MatchMode;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.creativedrive.model.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserCustomRepository 
{
	private final MongoOperations mongoOperations;
	
	public Iterable<User> buscar(User usr, Pageable pageable)
	{
		Query query = new Query();
		
		if(usr.getId() == null || !usr.getId().trim().isEmpty())
		{
			if(usr.getNome() != null && !usr.getNome().trim().isEmpty())
				query.addCriteria(Criteria.where("nome").regex(MongoRegexCreator.INSTANCE.toRegularExpression(
						usr.getNome(), MatchMode.CONTAINING), "i"));
			
			if(usr.getEndereco() != null && !usr.getEndereco().trim().isEmpty())
				query.addCriteria(Criteria.where("endereco").regex(MongoRegexCreator.INSTANCE.toRegularExpression(
						usr.getEndereco(), MatchMode.CONTAINING), "i"));
			
			if(usr.getEmail() != null && !usr.getEmail().trim().isEmpty())
				query.addCriteria(Criteria.where("email").regex(MongoRegexCreator.INSTANCE.toRegularExpression(
						usr.getEmail(), MatchMode.CONTAINING), "i"));
			
			if(usr.getTelefone() != null && !usr.getTelefone().trim().isEmpty())
				query.addCriteria(Criteria.where("telefone").regex(MongoRegexCreator.INSTANCE.toRegularExpression(
						usr.getTelefone(), MatchMode.CONTAINING), "i"));
			
			if(usr.getPerfil() != null && !usr.getPerfil().trim().isEmpty())
				query.addCriteria(Criteria.where("perfil").regex(MongoRegexCreator.INSTANCE.toRegularExpression(
						usr.getPerfil(), MatchMode.EXACT), "i"));
		}
		else
		{
			query.addCriteria(Criteria.where("id").regex(MongoRegexCreator.INSTANCE.toRegularExpression(
					usr.getId(), MatchMode.EXACT)));
		}
		
		query.with(pageable);		

		List<User> list = mongoOperations.find(query, User.class);

	    return PageableExecutionUtils.getPage(list, pageable,
	            () -> mongoOperations.count(query, User.class));

	}
}
