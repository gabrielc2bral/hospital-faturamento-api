# Paciente

## Descrição
Representa um paciente do hospital.

## Campos

ID - Long - gerado pelo banco\
nome - String - obrigatório\
CPF - String - obrigatório\
dataDeNascimento - LocalDate - obrigatório\
consultas - ArrayList - @OneToMany\
convenio - Convenio - @ManyToOne

## Regras
CPF deve ser único e válido 

## Relacionamentos
1:N com consultas (1 paciente pode ter várias consultas e 1 consulta apenas 1 paciente)