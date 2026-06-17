package br.com.monolithlabs.clinica.auth.controller;

import br.com.monolithlabs.clinica.auth.dto.request.CadastroDtoRequest;
import br.com.monolithlabs.clinica.auth.dto.request.UserEmailConfirmationDTO;
import br.com.monolithlabs.clinica.auth.entity.User;
import br.com.monolithlabs.clinica.auth.service.EmailConfirmationService;
import br.com.monolithlabs.clinica.auth.service.UserService;
import br.com.monolithlabs.clinica.paciente.dto.PacienteDTO;
import br.com.monolithlabs.clinica.paciente.service.PacienteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private final PacienteService pacienteService;

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

    @PostMapping("/cadastro/validar")
    public String validarCadastro(@ModelAttribute UserEmailConfirmationDTO dto) {
        emailConfirmationService.validarCodigoUsuario(dto);

        return "redirect:/login";
    }
    @GetMapping("/cadastro/concluir")
    public String cadastroConcluirPage(Model model) {
        model.addAttribute("paciente", new PacienteDTO());
        return "paciente/paciente_cadastro";
    }

    @PostMapping("/cadastro/concluir")
    public String concluirCadastro(@Valid @ModelAttribute("paciente") PacienteDTO dto,
                                   BindingResult result,
                                   @AuthenticationPrincipal User userLogado,
                                   HttpServletRequest request,
                                   HttpServletResponse response,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/cadastro_concluir";
        }
        try {
            pacienteService.concluirCadastro(dto, userLogado);
            userService.concluirCadastro(userLogado);
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

            return "redirect:/login?cadastroConcluido";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao concluir cadastro.");
            return "redirect:/auth/cadastro/concluir";
        }
    }


}
