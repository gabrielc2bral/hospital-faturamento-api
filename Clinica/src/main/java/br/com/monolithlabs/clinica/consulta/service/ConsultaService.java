package br.com.monolithlabs.clinica.consulta.service;

import br.com.monolithlabs.clinica.consulta.dto.request.AgendarConsultaRequest;
import br.com.monolithlabs.clinica.consulta.entity.Consulta;
import br.com.monolithlabs.clinica.consulta.entity.StatusConsulta;
import br.com.monolithlabs.clinica.consulta.exception.ConsultaNaoEncontradaException;
import br.com.monolithlabs.clinica.consulta.exception.HorarioIndisponivelException;
import br.com.monolithlabs.clinica.consulta.exception.OperacaoNaoPermitidaException;
import br.com.monolithlabs.clinica.consulta.repository.ConsultaRepository;
import br.com.monolithlabs.clinica.medico.entity.Medico;
import br.com.monolithlabs.clinica.medico.entity.HorarioTrabalhoMedico;
import br.com.monolithlabs.clinica.medico.exception.MedicoNaoEncontradoException;
import br.com.monolithlabs.clinica.medico.repository.HorarioTrabalhoMedicoRepository;
import br.com.monolithlabs.clinica.medico.repository.MedicoRepository;
import br.com.monolithlabs.clinica.paciente.entity.Paciente;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final HorarioTrabalhoMedicoRepository horarioRepository;

    @Value("${consulta.duracao-slot-minutos:30}")
    private int duracaoSlotMinutos;

    @Transactional(readOnly = true)
    public List<LocalTime> listarHorariosDisponiveis(Long medicoId, LocalDate data) {
        DayOfWeek diaDaSemana = data.getDayOfWeek();

        List<HorarioTrabalhoMedico> horariosTrabalho =
                horarioRepository.findByMedicoIdAndDiaDaSemana(medicoId, diaDaSemana);

        if (horariosTrabalho.isEmpty()) {
            return List.of();
        }

        List<Consulta> ocupadas =
                consultaRepository.findByMedicoIdAndDataAndStatus(medicoId, data, StatusConsulta.AGENDADA);

        Set<LocalTime> horariosOcupados = ocupadas.stream()
                .map(Consulta::getHorario)
                .collect(Collectors.toSet());

        List<LocalTime> disponiveis = new ArrayList<>();
        for (HorarioTrabalhoMedico bloco : horariosTrabalho) {
            LocalTime slot = bloco.getHoraInicio();
            while (!slot.plusMinutes(duracaoSlotMinutos).isAfter(bloco.getHoraFim())) {
                if (!horariosOcupados.contains(slot)) {
                    disponiveis.add(slot);
                }
                slot = slot.plusMinutes(duracaoSlotMinutos);
            }
        }

        disponiveis.sort(LocalTime::compareTo);
        return disponiveis;
    }

    @Transactional
    public Consulta agendar(Paciente paciente, AgendarConsultaRequest request) {
        Medico medico = medicoRepository.findById(request.getMedicoId())
                .orElseThrow(() -> new MedicoNaoEncontradoException(request.getMedicoId()));

        if (request.getData().isBefore(LocalDate.now())) {
            throw new HorarioIndisponivelException("Não é possível agendar consulta em data passada.");
        }

        List<LocalTime> disponiveis = listarHorariosDisponiveis(request.getMedicoId(), request.getData());
        if (!disponiveis.contains(request.getHorario())) {
            throw new HorarioIndisponivelException("O horário selecionado não está mais disponível.");
        }

        Consulta consulta = new Consulta();
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setData(request.getData());
        consulta.setHorario(request.getHorario());
        consulta.setStatus(StatusConsulta.AGENDADA);

        return consultaRepository.save(consulta);
    }

    @Transactional(readOnly = true)
    public List<Consulta> listarPorPaciente(Long pacienteId) {
        return consultaRepository.findByPacienteIdOrderByDataDescHorarioDesc(pacienteId);
    }

    @Transactional
    public void cancelar(Long consultaId, Paciente pacienteLogado) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ConsultaNaoEncontradaException(consultaId));

        if (!consulta.getPaciente().getId().equals(pacienteLogado.getId())) {
            throw new OperacaoNaoPermitidaException("Você não pode cancelar uma consulta de outro paciente.");
        }

        consulta.setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);
    }
}
