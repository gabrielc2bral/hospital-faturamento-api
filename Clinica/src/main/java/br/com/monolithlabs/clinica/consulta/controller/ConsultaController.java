package br.com.monolithlabs.clinica.consulta.controller;

import br.com.monolithlabs.clinica.auth.entity.User;
import br.com.monolithlabs.clinica.consulta.dto.request.AgendarConsultaRequest;
import br.com.monolithlabs.clinica.consulta.exception.HorarioIndisponivelException;
import br.com.monolithlabs.clinica.consulta.service.ConsultaService;
import br.com.monolithlabs.clinica.medico.exception.MedicoNaoEncontradoException;
import br.com.monolithlabs.clinica.medico.service.EspecialidadeService;
import br.com.monolithlabs.clinica.medico.service.MedicoService;
import br.com.monolithlabs.clinica.paciente.entity.Paciente;
import br.com.monolithlabs.clinica.paciente.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/paciente/consultas")
@PreAuthorize("hasRole('PACIENTE')")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;
    private final MedicoService medicoService;
    private final EspecialidadeService especialidadeService;
    private final PacienteService pacienteService;

    @GetMapping
    public String minhasConsultas(@AuthenticationPrincipal User userLogado, Model model) {
        Paciente paciente = pacienteService.buscarPorUsuario(userLogado);
        model.addAttribute("consultas", consultaService.listarPorPaciente(paciente.getId()));
        return "paciente/minhas_consultas";
    }

    @GetMapping("/nova")
    public String novaConsulta(Model model) {
        model.addAttribute("especialidades", especialidadeService.listarTodas());
        model.addAttribute("agendarConsulta", new AgendarConsultaRequest(null, null, null));
        return "paciente/agendar_consulta";
    }

    @GetMapping("/medicos")
    @ResponseBody
    public List<Map<String, Object>> medicosPorEspecialidade(@RequestParam Long especialidadeId) {
        return medicoService.listarPorEspecialidade(especialidadeId).stream()
                .map(m -> Map.<String, Object>of("id", m.getId(), "nome", m.getNome(), "crm", m.getCrm()))
                .toList();
    }

    @GetMapping("/horarios-disponiveis")
    @ResponseBody
    public List<String> horariosDisponiveis(
            @RequestParam Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return consultaService.listarHorariosDisponiveis(medicoId, data).stream()
                .map(h -> h.format(DateTimeFormatter.ofPattern("HH:mm")))
                .toList();
    }

    @PostMapping
    public String agendar(
            @AuthenticationPrincipal User userLogado,
            @Valid @ModelAttribute("agendarConsulta") AgendarConsultaRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("especialidades", especialidadeService.listarTodas());
            return "paciente/agendar_consulta";
        }

        try {
            Paciente paciente = pacienteService.buscarPorUsuario(userLogado);
            consultaService.agendar(paciente, request);
        } catch (MedicoNaoEncontradoException | HorarioIndisponivelException e) {
            model.addAttribute("erroGeral", e.getMessage());
            model.addAttribute("especialidades", especialidadeService.listarTodas());
            return "paciente/agendar_consulta";
        }

        redirectAttributes.addFlashAttribute("sucesso", "Consulta agendada com sucesso.");
        return "redirect:/paciente/consultas";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, @AuthenticationPrincipal User userLogado, RedirectAttributes redirectAttributes) {
        Paciente paciente = pacienteService.buscarPorUsuario(userLogado);
        consultaService.cancelar(id, paciente);
        redirectAttributes.addFlashAttribute("sucesso", "Consulta cancelada.");
        return "redirect:/paciente/consultas";
    }
}
