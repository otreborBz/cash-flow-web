package io.github.mds.cashflowweb;

import io.github.mds.cashflowweb.employee.EmployeeRepository;
import io.github.mds.cashflowweb.manager.ManagerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/js/**",
                                "/css/**",
                                "/img/**",
                                "/h2-console/**"
                        ).permitAll()
                        .anyRequest().hasRole("MANAGER")
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .failureUrl("/login?error")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .rememberMe(Customizer.withDefaults())
                .build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain mobileSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable) // there's no need for csrf protection for mobile applications
                .cors(AbstractHttpConfigurer::disable) // there's no need for cors protection for mobile applications
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/travels/{travelId}/expenses/{expenseId}/fiscalNote",
                                "/api/travels/{id}/print"
                        ).authenticated()
                        .anyRequest().hasRole("EMPLOYEE")
                )
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/api/login")
                        .usernameParameter("email")
                        .successHandler((request, response, authentication) -> response.setStatus(200))
                        .failureHandler((request, response, exception) -> response.setStatus(400))
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(204))
                        .permitAll()
                )
                .rememberMe(Customizer.withDefaults())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> response.setStatus(401))
                        .accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(403))
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(ManagerRepository managerRepository, EmployeeRepository employeeRepository) {
        return username -> {
          var manager = managerRepository.findByEmail(username).orElse(null);
          if (manager != null) {
              return manager;
          }
          var employee = employeeRepository.findByEmail(username).orElse(null);
          if (employee != null) {
              return employee;
          }
          throw new UsernameNotFoundException("User not found");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
