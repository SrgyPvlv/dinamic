package com.example.sbkafka.Model;

import javax.persistence.*;

@Entity
@Table(name="users")
public class Users {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column
	private String login;
	
	@Column
	private String password;
	
	@Column
	private String role;
	
	public Users() {}
	
	public Users(String login,String password,String role) {
		this.login=login;
		this.password=password;
		this.role=role;
	}
	
	public int getId() {
		return id;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login=login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password=password;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role=role;
	}
}
