# Paciente

## Descrição
Representa um paciente do hospital.

## Campos

ID - Long - gerado pelo banco\
nome - String - obrigatório\
CPF - String - obrigatório\
dataDeNascimento - LocalDate - obrigatório

## Regras
CPF deve ser único e válido 

## Relacionamentos
1:N com atendimento (1 paciente pode ter vários atendimentos e 1 atendimento apenas 1 paciente)