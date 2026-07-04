package br.com.monolithlabs.clinica.medico.service;

import br.com.monolithlabs.clinica.medico.dto.request.CadastroHorarioTrabalhoRequest;
import br.com.monolithlabs.clinica.medico.entity.HorarioTrabalhoMedico;
import br.com.monolithlabs.clinica.medico.entity.Medico;
import br.com.monolithlabs.clinica.medico.exception.HorarioInvalidoException;
import br.com.monolithlabs.clinica.medico.exception.HorarioNaoEncontradoException;
import br.com.monolithlabs.clinica.medico.exception.HorarioSobrepostoException;
import br.com.monolithlabs.clinica.medico.exception.MedicoNaoEncontradoException;
import br.com.monolithlabs.clinica.medico.repository.HorarioTrabalhoMedicoRepository;
import br.com.monolithlabs.clinica.medico.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioTrabalhoMedicoService {

    private final HorarioTrabalhoMedicoRepository horarioRepository;
    private final MedicoRepository medicoRepository;

    @Transactional(readOnly = true)
    public List<HorarioTrabalhoMedico> listarPorMedico(Long medicoId) {
        return horarioRepository.findByMedicoIdOrderByDiaDaSemanaAscHoraInicioAsc(medicoId);
    }

    @Transactional
    public HorarioTrabalhoMedico adicionar(Long medicoId, CadastroHorarioTrabalhoRequest request) {
        if (!request.getHoraInicio().isBefore(request.getHoraFim())) {
            throw new HorarioInvalidoException("A hora de início deve ser anterior à hora de fim.");
        }

        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new MedicoNaoEncontradoException(medicoId));

        List<HorarioTrabalhoMedico> existentes =
                horarioRepository.findByMedicoIdAndDiaDaSemana(medicoId, request.getDiaDaSemana());

        boolean sobrepoe = existentes.stream().anyMatch(h ->
                request.getHoraInicio().isBefore(h.getHoraFim()) &&
                        h.getHoraInicio().isBefore(request.getHoraFim())
        );
        if (sobrepoe) {
            throw new HorarioSobrepostoException(request.getDiaDaSemana());
        }

        HorarioTrabalhoMedico horario = new HorarioTrabalhoMedico();
        horario.setDiaDaSemana(request.getDiaDaSemana());
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFim(request.getHoraFim());
        horario.setMedico(medico);

        return horarioRepository.save(horario);
    }

    @Transactional
    public void remover(Long medicoId, Long horarioId) {
        HorarioTrabalhoMedico horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new HorarioNaoEncontradoException(horarioId));

        if (!horario.getMedico().getId().equals(medicoId)) {
            throw new HorarioNaoEncontradoException(horarioId);
        }

        horarioRepository.delete(horario);
    }
}
