package br.com.monolithlabs.clinica.medico.exception;

public class MedicoNaoEncontradoException extends RuntimeException {

    public MedicoNaoEncontradoException(Long id) {
        super("Médico não encontrado com o id: " + id);
    }
}