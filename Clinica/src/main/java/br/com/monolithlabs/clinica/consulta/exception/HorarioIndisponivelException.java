package br.com.monolithlabs.clinica.consulta.exception;

public class HorarioIndisponivelException extends RuntimeException {

    public HorarioIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
