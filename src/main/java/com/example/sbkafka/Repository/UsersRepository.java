package com.example.sbkafka.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sbkafka.Model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {
	
	public Users findByLogin(String login);
	
}
