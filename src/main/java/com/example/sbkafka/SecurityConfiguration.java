package com.example.sbkafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	CustomUserDetailsService userDetailsService;
	
	@Autowired
    private AccessDeniedHandler accessDeniedHandler;
	
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService);
    } 
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/admin","/admin/*","/bdDelete","/bdEdit", "/orderEdit", "/fileLoad","/fileDelete").hasRole( "ADMIN")
        .antMatchers("/recordOrder","/mailSend","/bdShow","/downloadexel","/fileDownload","/findByBsName","/findByOrderNumber").hasAnyRole("USER", "ADMIN")
        .antMatchers("/calcOrder").hasAnyRole("USER", "ADMIN","CONTRACTOR")
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
