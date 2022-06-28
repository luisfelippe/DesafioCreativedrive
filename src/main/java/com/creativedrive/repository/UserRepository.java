package com.creativedrive.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.creativedrive.model.User;

public interface UserRepository extends MongoRepository<User, String>
{
    User findByNome(String nome);
}
