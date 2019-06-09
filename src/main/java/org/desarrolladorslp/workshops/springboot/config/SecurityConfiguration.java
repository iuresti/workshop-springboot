package org.desarrolladorslp.workshops.springboot.config;

import org.desarrolladorslp.workshops.springboot.security.RequestUtils;
import org.desarrolladorslp.workshops.springboot.security.RestAuthenticationEntryPoint;
import org.desarrolladorslp.workshops.springboot.security.jwt.JwtFilter;
import org.desarrolladorslp.workshops.springboot.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;
    private final RequestUtils requestUtils;
    private final RestAuthenticationEntryPoint entryPoint;

    public SecurityConfiguration(UserDetailsService userDetailsService,
                                 TokenProvider tokenProvider,
                                 RequestUtils requestUtils,
                                 RestAuthenticationEntryPoint entryPoint) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.requestUtils = requestUtils;
        this.entryPoint = entryPoint;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
        managerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/api/**");
        web.ignoring().antMatchers(HttpMethod.GET, "/**/*.css", "/**/*.js", "/**/*.map", "/**/*.html");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint)
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/jsondoc").permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private JwtFilter jwtFilter() {
        return new JwtFilter(tokenProvider, requestUtils);
    }

}
