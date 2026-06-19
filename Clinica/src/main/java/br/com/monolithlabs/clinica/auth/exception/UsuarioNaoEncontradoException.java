package br.com.monolithlabs.clinica.auth.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(String email) {
        super("Nenhum usuário encontrado com o e-mail: " + email);
    }
}