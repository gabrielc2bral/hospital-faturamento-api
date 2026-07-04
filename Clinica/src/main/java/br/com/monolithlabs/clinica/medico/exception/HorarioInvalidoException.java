package br.com.monolithlabs.clinica.medico.exception;

public class HorarioInvalidoException extends RuntimeException {

    public HorarioInvalidoException(String mensagem) {
        super(mensagem);
    }
}
