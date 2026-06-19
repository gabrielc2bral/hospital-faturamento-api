package br.com.monolithlabs.clinica.medico.controller;

import br.com.monolithlabs.clinica.auth.exception.UsuarioNotEnabledException;
import br.com.monolithlabs.clinica.medico.dto.request.AtualizarMedicoRequest;
import br.com.monolithlabs.clinica.medico.dto.request.CadastroMedicoRequest;
import br.com.monolithlabs.clinica.medico.entity.Medico;
import br.com.monolithlabs.clinica.medico.exception.EspecialidadeNaoEncontradaException;
import br.com.monolithlabs.clinica.auth.exception.UsuarioNaoEncontradoException;
import br.com.monolithlabs.clinica.auth.exception.UsuarioNaoPendenteException;
import br.com.monolithlabs.clinica.medico.service.EspecialidadeService;
import br.com.monolithlabs.clinica.medico.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/admin/medicos")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;
    private final EspecialidadeService especialidadeService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("medicos", medicoService.listarTodos());
        return "admin/medicos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("cadastroMedico", new CadastroMedicoRequest("", "", null, ""));
        adicionarEspecialidades(model);

        return "admin/medicos/cadastro";
    }

    @PostMapping
    public String cadastrar(@Valid @ModelAttribute("cadastroMedico") CadastroMedicoRequest dto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            adicionarEspecialidades(model);
            return "admin/medicos/cadastro";
        }

        try {
            medicoService.cadastrar(dto);
        } catch (UsuarioNaoEncontradoException | UsuarioNaoPendenteException | EspecialidadeNaoEncontradaException | UsuarioNotEnabledException e) {
            model.addAttribute("erroGeral", e.getMessage());
            adicionarEspecialidades(model);
            return "admin/medicos/cadastro";
        }

        log.info("Médico cadastrado: {}", dto.getNome());
        redirectAttributes.addFlashAttribute("sucesso", "Médico cadastrado com sucesso.");

        return "redirect:/admin/medicos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Medico medico = medicoService.buscarPorId(id);

        AtualizarMedicoRequest request = new AtualizarMedicoRequest(medico.getNome(), medico.getCrm(), medico.getEspecialidade().getId());

        model.addAttribute("medico", request);
        adicionarAtributosDeEdicao(model, medico);

        return "admin/medicos/editar";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute("medico") AtualizarMedicoRequest request, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            adicionarAtributosDeEdicao(model, medicoService.buscarPorId(id));
            return "admin/medicos/editar";
        }

        try {
            medicoService.atualizar(id, request);
        } catch (EspecialidadeNaoEncontradaException e) {
            model.addAttribute("erroGeral", e.getMessage());
            adicionarAtributosDeEdicao(model, medicoService.buscarPorId(id));
            return "admin/medicos/editar";
        }

        log.info("Médico {} atualizado", id);
        redirectAttributes.addFlashAttribute("sucesso", "Médico atualizado com sucesso.");

        return "redirect:/admin/medicos";
    }

    private void adicionarEspecialidades(Model model) {
        model.addAttribute("especialidades", especialidadeService.listarTodas());
    }

    private void adicionarAtributosDeEdicao(Model model, Medico medico) {
        model.addAttribute("medicoId", medico.getId());
        model.addAttribute("email", medico.getUser().getEmail());
        adicionarEspecialidades(model);
    }
}