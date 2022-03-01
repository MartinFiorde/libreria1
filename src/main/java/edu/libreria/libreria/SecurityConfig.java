package edu.libreria.libreria;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSec) throws Exception {

        httpSec.httpBasic().disable();
        /*      COMANDO CREADO PARA DESHABILITAR MENSAJE DE LOGIN AUTOMATICO DE SPRING AL TIPEAR LA URL EN EL EXPLORADOR
        https://stackoverflow.com/questions/23636368/how-to-disable-spring-security-login-screen        */
        
        httpSec.headers().frameOptions().sameOrigin()
                .and().authorizeRequests()
                            .antMatchers("/css/*", "/js/*", "/img/*")
                            .permitAll()
                .and().formLogin()
                            .loginPage("/login")
                            .loginProcessingUrl("/logincheck")
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .defaultSuccessUrl("/inicio")
                            .permitAll()
                .and().logout()
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/")
                            .permitAll();
    }

}
