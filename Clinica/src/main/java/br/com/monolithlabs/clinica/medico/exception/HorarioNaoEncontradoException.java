package br.com.monolithlabs.clinica.medico.exception;

public class HorarioNaoEncontradoException extends RuntimeException {

    public HorarioNaoEncontradoException(Long id) {
        super("Horário de trabalho não encontrado com o id: " + id);
    }
}
