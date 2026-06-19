package br.com.monolithlabs.clinica.medico.exception;

import br.com.monolithlabs.clinica.medico.controller.EspecialidadeController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Captura exceções específicas do fluxo de Especialidade e converte em
 * um redirecionamento amigável, em vez de deixar a exceção subir até a
 * página de erro padrão do Spring (whitelabel).
 */
@ControllerAdvice(assignableTypes = EspecialidadeController.class)
public class EspecialidadeExceptionHandler {

  @ExceptionHandler(EspecialidadeNaoEncontradaException.class)
  public String tratarNaoEncontrada(
          EspecialidadeNaoEncontradaException ex,
          RedirectAttributes redirectAttributes
  ) {
    redirectAttributes.addFlashAttribute("erroGeral", ex.getMessage());
    return "redirect:/admin/especialidades";
  }
}