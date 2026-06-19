package br.com.monolithlabs.clinica.medico.exception;

public class EspecialidadeNaoEncontradaException extends RuntimeException {

    public EspecialidadeNaoEncontradaException(Long id) {
        super("Especialidade não encontrada com o id: " + id);
    }
}
