package br.com.monolithlabs.clinica.medico.controller;

import br.com.monolithlabs.clinica.medico.dto.request.AtualizarEspecialidadeRequest;
import br.com.monolithlabs.clinica.medico.dto.request.CadastroEspecialidadeRequest;
import br.com.monolithlabs.clinica.medico.entity.Especialidade;
import br.com.monolithlabs.clinica.medico.service.EspecialidadeService;
import br.com.monolithlabs.clinica.medico.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/admin/especialidades")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class EspecialidadeController {

    // Mensagem usada caso exista (ou venha a existir) uma constraint
    // de unicidade no campo "nome" da entidade Especialidade.
    private static final String ERRO_NOME_DUPLICADO =
            "Já existe uma especialidade cadastrada com este nome.";

    private final EspecialidadeService especialidadeService;
    private final MedicoService medicoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("especialidades", especialidadeService.listarTodas());
        return "admin/especialidades/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("cadastroEspecialidade", new CadastroEspecialidadeRequest(""));
        return "admin/especialidades/cadastro";
    }

    @PostMapping
    public String cadastrar(
            @Valid @ModelAttribute("cadastroEspecialidade") CadastroEspecialidadeRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/especialidades/cadastro";
        }

        try {
            especialidadeService.cadastrar(request);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("erroGeral", ERRO_NOME_DUPLICADO);
            return "admin/especialidades/cadastro";
        }

        log.info("Especialidade cadastrada: {}", request.getNome());
        redirectAttributes.addFlashAttribute("sucesso", "Especialidade cadastrada com sucesso.");

        return "redirect:/admin/especialidades";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Especialidade especialidade = especialidadeService.buscarPorId(id);

        model.addAttribute("especialidade", new AtualizarEspecialidadeRequest(especialidade.getNome()));
        adicionarAtributosDeEdicao(model, especialidade);

        return "admin/especialidades/editar";
    }

    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("especialidade") AtualizarEspecialidadeRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            adicionarAtributosDeEdicao(model, especialidadeService.buscarPorId(id));
            return "admin/especialidades/editar";
        }

        try {
            especialidadeService.atualizar(id, request);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("erroGeral", ERRO_NOME_DUPLICADO);
            adicionarAtributosDeEdicao(model, especialidadeService.buscarPorId(id));
            return "admin/especialidades/editar";
        }

        log.info("Especialidade {} atualizada", id);
        redirectAttributes.addFlashAttribute("sucesso", "Especialidade atualizada com sucesso.");

        return "redirect:/admin/especialidades";
    }

    private void adicionarAtributosDeEdicao(Model model, Especialidade especialidade) {
        model.addAttribute("especialidadeId", especialidade.getId());
        model.addAttribute("nomeAtual", especialidade.getNome());
        model.addAttribute("quantidadeMedicos", medicoService.contarPorEspecialidade(especialidade.getId()));
    }
}