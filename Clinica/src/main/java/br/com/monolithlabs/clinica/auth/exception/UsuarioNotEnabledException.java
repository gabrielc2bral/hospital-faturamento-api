package br.com.monolithlabs.clinica.auth.exception;

public class UsuarioNotEnabledException extends RuntimeException {
    public UsuarioNotEnabledException(String email) {
      super("O usuário com e-mail " + email + " não validou o email");
    }
}
