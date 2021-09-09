package com.example.sbkafka;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

	@Autowired
	UsersRepository usersrepository;
	
    public UsersService(UsersRepository usersrepository) {
		this.usersrepository=usersrepository;
	}
	
	public List<Users> findAllUsers(){
		return usersrepository.findAll();

	}
	
}
