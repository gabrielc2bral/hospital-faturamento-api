package br.com.monolithlabs.clinica.auth.controller;

import br.com.monolithlabs.clinica.auth.dto.request.CadastroDtoRequest;
import br.com.monolithlabs.clinica.auth.dto.request.UserEmailConfirmationDTO;
import br.com.monolithlabs.clinica.auth.service.EmailConfirmationService;
import br.com.monolithlabs.clinica.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final EmailConfirmationService emailConfirmationService;

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("usuario", new CadastroDtoRequest());
        return "auth/cadastro_user";
    }

    @PostMapping("/cadastro")
    public String postUser(CadastroDtoRequest dto, RedirectAttributes redirectAttributes) {

        try {
            userService.cadastrarUsuario(dto);
            redirectAttributes.addFlashAttribute("mensagemSucesso","Usuário cadastrado com sucesso, verifique o seu email e coloque o seu código!");
            return "redirect:/auth/cadastro/validar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar usuário!");
            return "redirect:/auth/cadastro";
        }
    }

    @GetMapping("/cadastro/validar")
    public String validarPage(Model model) {

        UserEmailConfirmationDTO dto = new UserEmailConfirmationDTO();

        model.addAttribute("usuario", dto);

        return "auth/validar";
    }
    @GetMapping("/cadastro/concluir")
    public String cadastroConcluirPage(Model model) {

        return "home/adm";
    }

    @PostMapping("/cadastro/validar")
    public String validarCadastro(@ModelAttribute UserEmailConfirmationDTO dto) {
        emailConfirmationService.validarCodigoUsuario(dto);

        return "redirect:/login";
    }


}
