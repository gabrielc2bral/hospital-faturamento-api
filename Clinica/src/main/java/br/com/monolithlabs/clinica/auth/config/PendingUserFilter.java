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

        boolean isAnonymous = auth == null || auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken;
        if (isAnonymous) {
            filterChain.doFilter(request, response);
            return;
        }
        boolean isPending = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PENDING"));
        boolean isPublicPage  = uri.equals("/") || uri.equals("/sobre") || uri.equals("/contato");
        boolean isAuthPage = uri.startsWith("/auth/login") || uri.startsWith("/auth/cadastro") || uri.startsWith("/auth/cadastro/validar");
        boolean isConcluir = uri.startsWith("/auth/cadastro/concluir");
        if (isPending) {
            if (isAuthPage && !isConcluir){
                response.sendRedirect("/auth/cadastro/concluir");
                return;
            }
            if (isPublicPage || isConcluir || uri.equals("/logout")) {
                filterChain.doFilter(request, response);
                return;
            }
            response.sendRedirect("/auth/cadastro/concluir");
            return;
        }
        if (isAuthPage) {
            response.sendRedirect("/");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
