package com.example.webshop.configurations;

import com.example.webshop.services.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // prePostEnabled är true som standard //@EnableMethodSecurity(jsr250Enabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
               // .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/registration", "/user/**", "/product/**", "/images/**", "/static/**").permitAll() // Adds "/images/**" and "/static/**" to the list of paths that do not require authentication.
                        .anyRequest().authenticated() // All other requests require authentication.
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll() // Allows all users to access the login page.
                )
                .logout((logout) -> logout
                        .permitAll() // Allows all users to logout.
                );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}


/*
-------------------------------------
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/registration", "/user/**", "/product/**").permitAll()
                        .requestMatchers("/image/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
  -----------------------------------

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigureAdapter {
    private final CustomUserDetailsService userDetailsService;

        @Override
        protected void configure(HttpSecurity http) throws Exception{
            http
                    .authorizeRequests()
                    .antMatchers("/", "/product/**", "/images/**", "/registration")
                    .permittAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permittAll()
                    .and()
                    .logout()
                    .permittAll();
        }

               @Override
         protected void configure(AuthenticationManagerBuilder auth) throws Exception{
          auth.userDetailsService(userDetailsService)
          .passwordEncoder(passwordEncoder());
          }

          @Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/registration", "/user/**" ).permitAll()
                        .requestMatchers( "/image/**", "/product/**", "/image/**")
                        .hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    / protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     //   auth.userDetailsService(userDetailsService)
       //         .passwordEncoder(passwordEncoder());
    //}
 */