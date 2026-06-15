package br.com.monolithlabs.clinica.auth.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var auth = authentication.getAuthorities();
        boolean isPending = auth.contains(new SimpleGrantedAuthority("ROLE_PENDING"));
        boolean isAdmin = auth.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isMedico = auth.contains(new SimpleGrantedAuthority("ROLE_MEDICO"));

        if (isPending) {
            response.sendRedirect("/auth/cadastro/concluir");
            return;
        }

        if (isAdmin) {
            response.sendRedirect("/admin/dashboard");
            return;
        }

        if (isMedico) {
            response.sendRedirect("/medico/home");
            return;
        }

        response.sendRedirect("/home");
    }
}
