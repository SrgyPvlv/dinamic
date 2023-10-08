package com.example.sbkafka.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sbkafka.Model.Users;
import com.example.sbkafka.Repository.UsersRepository;

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
	
	public void saveUsers(Users users) {
		usersrepository.saveAndFlush(users);
	}
	
	public Users findUsersById(int id) {
		return usersrepository.getById(id);
	
	}
	
	public void deleteUserById(int id) {
		usersrepository.deleteById(id);
	}
	
	public Users findUsersByLogin(String login) { //получение пользователя по логину
		return usersrepository.findByLogin(login);
	}
	
}
