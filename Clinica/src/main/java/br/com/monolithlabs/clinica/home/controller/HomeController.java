package br.com.monolithlabs.clinica.home.controller;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        boolean logado = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        boolean pendente = logado && authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_PENDING"));

        model.addAttribute("logado", logado);
        model.addAttribute("pendente", pendente);

        return "home/index";
    }

}
