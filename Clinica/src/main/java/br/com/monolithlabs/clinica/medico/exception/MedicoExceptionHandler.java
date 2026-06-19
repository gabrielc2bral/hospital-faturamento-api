package br.com.monolithlabs.clinica.medico.exception;

import br.com.monolithlabs.clinica.medico.controller.MedicoController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Captura exceções específicas do fluxo de Médico e converte em
 * um redirecionamento amigável, em vez de deixar a exceção subir até a
 * página de erro padrão do Spring (whitelabel).
 */
@ControllerAdvice(assignableTypes = MedicoController.class)
public class MedicoExceptionHandler {

    @ExceptionHandler(MedicoNaoEncontradoException.class)
    public String tratarNaoEncontrado(
            MedicoNaoEncontradoException ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("erroGeral", ex.getMessage());
        return "redirect:/admin/medicos";
    }
}