package br.com.monolithlabs.clinica.consulta.exception;

public class ConsultaNaoEncontradaException extends RuntimeException {

    public ConsultaNaoEncontradaException(Long id) {
        super("Consulta não encontrada com o id: " + id);
    }
}
