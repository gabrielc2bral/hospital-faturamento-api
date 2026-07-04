package br.com.monolithlabs.clinica.paciente.exception;

public class PacienteNaoEncontradoException extends RuntimeException {

    public PacienteNaoEncontradoException(String email) {
        super("Cadastro de paciente não encontrado para o usuário: " + email);
    }
}
