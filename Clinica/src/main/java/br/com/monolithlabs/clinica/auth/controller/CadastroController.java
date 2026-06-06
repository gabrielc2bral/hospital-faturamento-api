package br.com.monolithlabs.clinica.auth.controller;

import br.com.monolithlabs.clinica.auth.dto.request.UserDtoRequest;
import br.com.monolithlabs.clinica.auth.dto.request.UserEmailConfirmationDTO;
import br.com.monolithlabs.clinica.auth.service.EmailConfirmationService;
import br.com.monolithlabs.clinica.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CadastroController {

    private final UserService userService;
    private final EmailConfirmationService emailConfirmationService;

    @GetMapping("/cadastro")
    public String cadastro(Model model){
        model.addAttribute("usuario", new UserDtoRequest());
        return "auth/cadastro_user";
    }
    @PostMapping("/cadastro")
    public String postUser(@ModelAttribute UserDtoRequest dto) {
        userService.cadastrarUsuario(dto);

        return "redirect:/cadastro/validar";
    }

    @GetMapping("/cadastro/validar")
    public String validarPage(Model model) {

        UserEmailConfirmationDTO dto = new UserEmailConfirmationDTO();

        model.addAttribute("usuario", dto);

        return "auth/validar";
    }

    @PostMapping("/cadastro/validar")
    public String validarCadastro(@ModelAttribute UserEmailConfirmationDTO dto) {
        emailConfirmationService.validarCodigoUsuario(dto);

        return "home/index";
    }
}
