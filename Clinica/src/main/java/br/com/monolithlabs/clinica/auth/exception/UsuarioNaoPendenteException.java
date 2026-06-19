package br.com.monolithlabs.clinica.auth.exception;

public class UsuarioNaoPendenteException extends RuntimeException {

  public UsuarioNaoPendenteException(String email) {
    super("O usuário com e-mail " + email
            + " já possui um cadastro ativo e não pode ser vinculado novamente.");
  }
}