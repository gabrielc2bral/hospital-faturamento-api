CREATE TABLE horario_trabalho_medico (
    id BIGSERIAL PRIMARY KEY,
    dia_da_semana VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    medico_id BIGINT NOT NULL REFERENCES medico(id)
);

CREATE TABLE consulta (
    id BIGSERIAL PRIMARY KEY,
    data DATE NOT NULL,
    horario TIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    medico_id BIGINT NOT NULL REFERENCES medico(id),
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id)
);

CREATE INDEX idx_horario_trabalho_medico_id ON horario_trabalho_medico(medico_id);
CREATE INDEX idx_consulta_medico_data ON consulta(medico_id, data);
CREATE INDEX idx_consulta_paciente_id ON consulta(paciente_id);
