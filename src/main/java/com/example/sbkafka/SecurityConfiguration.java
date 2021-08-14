package com.example.sbkafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private AccessDeniedHandler accessDeniedHandler;

	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;}
	
	@Autowired      // here is configuration related to spring boot basic authentication
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		String passwordAdmin = "mts2001@";
		String passwordUser = "apgrbs123";
		 
        String encrytedPasswordAdmin = this.passwordEncoder().encode(passwordAdmin);
		String encrytedPasswordUser = this.passwordEncoder().encode(passwordUser);
         
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> //
        mngConfig = auth.inMemoryAuthentication();
 
        UserDetails u1 = User.withUsername("admin").password(encrytedPasswordAdmin).roles("ADMIN").build();
        UserDetails u2 = User.withUsername("rbs").password(encrytedPasswordUser).roles("USER").build();
 
        mngConfig.withUser(u1);
        mngConfig.withUser(u2);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/admin","/admin/priceShow","/admin/priceChanges","/admin/bdEdit","/bdDelete","/bdEdit").hasRole( "ADMIN")
        .antMatchers("/calcOrder","/mailSend","/bdShow").hasAnyRole("USER", "ADMIN")
        .antMatchers("/").permitAll()
        .and().formLogin()
        .loginPage("/login")
        .defaultSuccessUrl("/calcOrder")
        .and()
        .logout()
        .permitAll()
        .and()
        .exceptionHandling().accessDeniedHandler(accessDeniedHandler);;
        
        
   }
	
}
