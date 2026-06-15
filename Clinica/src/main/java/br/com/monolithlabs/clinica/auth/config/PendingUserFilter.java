package br.com.monolithlabs.clinica.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class PendingUserFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()){
            boolean isPending = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PENDING"));
            boolean isOnboardingPage = uri.startsWith("/auth/cadastro/concluir");

            if (isPending && !isOnboardingPage){
                response.sendRedirect("/auth/cadastro/concluir");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
