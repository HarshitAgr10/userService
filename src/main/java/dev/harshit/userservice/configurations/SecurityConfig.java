package dev.harshit.userservice.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> {
                    try {
                        requests
                                .anyRequest().permitAll()
                                .and().cors().disable()
                                .csrf().disable();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
        });

        return http.build();
    }
}





/* *
* @Configuration: Tells Spring that this class contains configuration settings, specifically for
                  setting up security in the application.

* securityFilterChain() takes an HttpSecurity object as a parameter. This object is used to
   configure the security settings for HTTP requests.

* authorizeHttpRequests((requests) -> { ... }): This method is used to configure request
         authorization rules. The lambda expression (requests) -> { ... } is used to specify the
         authorization rules for HTTP requests.
* requests.anyRequest().permitAll(): This line configures the security to allow all incoming
          requests without any authentication. Essentially, it disables security for all endpoints.
* and().cors().disable(): This line disables Cross-Origin Resource Sharing (CORS) support. CORS is a
          security feature that restricts how resources on a web page can be requested from another
          domain outside the domain from which the resource originated.
* csrf().disable(): This line disables Cross-Site Request Forgery (CSRF) protection. CSRF is a type
          of attack that forces an end user to execute unwanted actions on a web application in
          which they're currently authenticated.

* http.build(): Method returns the configured HttpSecurity object by calling the build() method,
                which builds the security filter chain.
* */