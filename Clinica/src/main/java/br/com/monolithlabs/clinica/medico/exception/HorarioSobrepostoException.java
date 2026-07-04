package br.com.monolithlabs.clinica.medico.exception;

import java.time.DayOfWeek;

public class HorarioSobrepostoException extends RuntimeException {

    public HorarioSobrepostoException(DayOfWeek dia) {
        super("Já existe um horário cadastrado que se sobrepõe a este no dia informado: " + dia);
    }
}
