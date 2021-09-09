package com.example.sbkafka;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {

	@Query("select u from Users u where u.login=:login")
	public Users findByLogin(@Param("login") String login);
	
}
