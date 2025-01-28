package ru.netology.cloudservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.netology.cloudservice.constants.Endpoints;
import ru.netology.cloudservice.handlers.AuthFailProvider;
import ru.netology.cloudservice.handlers.AuthSuccessProvider;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    public final DataSource dataSource;

    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select login, password "
                        + "from users where login = ?");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .allowedOrigins("http://localhost:8081")
                        .allowedMethods(CorsConfiguration.ALL)
                        .allowedHeaders(CorsConfiguration.ALL);
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(Endpoints.LOGIN_URL).permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage(Endpoints.LOGIN_URL)
                        .loginProcessingUrl(Endpoints.LOGIN_URL)
                        .defaultSuccessUrl("/")
                        .successHandler(new AuthSuccessProvider())
                        .failureHandler(new AuthFailProvider())
                        .permitAll())
                .logout(logout -> logout.logoutUrl(Endpoints.LOGOUT_URL)
                        .logoutSuccessHandler((HttpServletRequest request, HttpServletResponse response,
                                               Authentication authentication) -> response.setStatus(HttpStatus.OK.value()))
                        .logoutSuccessUrl("/"));
        return http.build();
    }
}