package br.com.monolithlabs.clinica.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PendingUserFilter pendingUserFilter, CustomLoginSuccessHandler successHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/sobre",
                                "/contato",
                                "/login",
                                "/auth/cadastro",
                                "/auth/cadastro/validar",
                                "/h2-console/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/webjars/**"
                        ).permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/medico/**").hasRole("MEDICO")
                        .anyRequest().authenticated()
                ).addFilterBefore(pendingUserFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll()
                ).logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            boolean isPending = authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PENDING"));
                            String destino = isPending ? "/login?cadastroConcluido" : "/login?logout";
                            response.sendRedirect(destino);
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/login")
                        .setViewName("auth/login");
            }
        };
    }
}