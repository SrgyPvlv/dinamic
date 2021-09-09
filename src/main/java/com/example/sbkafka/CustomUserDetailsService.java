package com.example.sbkafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UsersRepository usersrepository;
	
	public CustomUserDetailsService(UsersRepository usersrepository) {
		this.usersrepository=usersrepository;
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Users users=usersrepository.findByLogin(login);
		
		if(users==null) {
		throw new UsernameNotFoundException ("Unknown user: "+ login);}
		UserDetails user=User.builder()
				.username(users.getLogin())
				.password(users.getPassword())
				.roles(users.getRole())
				.build();
		return user;
	}
	
	
	
}
