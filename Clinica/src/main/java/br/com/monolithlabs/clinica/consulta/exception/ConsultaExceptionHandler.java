package br.com.monolithlabs.clinica.consulta.exception;

import br.com.monolithlabs.clinica.consulta.controller.ConsultaController;
import br.com.monolithlabs.clinica.paciente.exception.PacienteNaoEncontradoException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Captura exceções específicas do fluxo de Consulta e converte em
 * um redirecionamento amigável, em vez de deixar a exceção subir até a
 * página de erro padrão do Spring (whitelabel).
 */
@ControllerAdvice(assignableTypes = ConsultaController.class)
public class ConsultaExceptionHandler {

    @ExceptionHandler({ConsultaNaoEncontradaException.class, OperacaoNaoPermitidaException.class})
    public String tratarErroConsulta(RuntimeException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("erroGeral", ex.getMessage());
        return "redirect:/paciente/consultas";
    }

    @ExceptionHandler(PacienteNaoEncontradoException.class)
    public String tratarPacienteNaoEncontrado(PacienteNaoEncontradoException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
        return "redirect:/";
    }
}
