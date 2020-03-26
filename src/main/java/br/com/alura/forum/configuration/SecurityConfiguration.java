package br.com.alura.forum.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("eduardo")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .authorities("ROLE_ADMIN")
            .and()
                .withUser("teste")
                .password(new BCryptPasswordEncoder().encode("654321"))
                .authorities("ROLE_ADMIN")
            .and()
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}