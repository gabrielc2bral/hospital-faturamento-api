package br.com.monolithlabs.clinica.medico.service;

import br.com.monolithlabs.clinica.auth.entity.User;
import br.com.monolithlabs.clinica.auth.enums.UserRole;
import br.com.monolithlabs.clinica.auth.exception.UsuarioNaoEncontradoException;
import br.com.monolithlabs.clinica.auth.exception.UsuarioNaoPendenteException;
import br.com.monolithlabs.clinica.auth.exception.UsuarioNotEnabledException;
import br.com.monolithlabs.clinica.auth.repository.UserRepository;
import br.com.monolithlabs.clinica.medico.dto.request.AtualizarMedicoRequest;
import br.com.monolithlabs.clinica.medico.dto.request.CadastroMedicoRequest;
import br.com.monolithlabs.clinica.medico.entity.Especialidade;
import br.com.monolithlabs.clinica.medico.entity.Medico;
import br.com.monolithlabs.clinica.medico.exception.EspecialidadeNaoEncontradaException;
import br.com.monolithlabs.clinica.medico.exception.MedicoNaoEncontradoException;
import br.com.monolithlabs.clinica.medico.repository.EspecialidadeRepository;
import br.com.monolithlabs.clinica.medico.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final UserRepository userRepository;
    private final EspecialidadeRepository especialidadeRepository;

    @Transactional(readOnly = true)
    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    @Transactional
    public void cadastrar(CadastroMedicoRequest dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new UsuarioNaoEncontradoException(dto.getEmail()));

        if (user.getRole() != UserRole.PENDING) {
            throw new UsuarioNaoPendenteException(dto.getEmail());
        }

        if (user.isEnabled()) {
            throw new UsuarioNotEnabledException(dto.getEmail());
        }

        Especialidade especialidade = especialidadeRepository.findById(dto.getEspecialidadeId()).orElseThrow(() -> new EspecialidadeNaoEncontradaException(dto.getEspecialidadeId()));

        Medico medico = new Medico();
        medico.setNome(dto.getNome());
        medico.setUser(user);
        medico.setCrm(dto.getCrm());
        medico.setEspecialidade(especialidade);

        medicoRepository.save(medico);

        user.setRole(UserRole.MEDICO);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public long contarPorEspecialidade(Long especialidadeId) {
        return medicoRepository.countByEspecialidadeId(especialidadeId);
    }

    @Transactional(readOnly = true)
    public Medico buscarPorId(Long id) {
        return medicoRepository.findById(id).orElseThrow(() -> new MedicoNaoEncontradoException(id));
    }

    @Transactional
    public void atualizar(Long id, AtualizarMedicoRequest request) {
        Medico medico = buscarPorId(id);

        Especialidade especialidade = especialidadeRepository.findById(request.getEspecialidadeId()).orElseThrow(() -> new EspecialidadeNaoEncontradaException(request.getEspecialidadeId()));

        medico.setNome(request.getNome());
        medico.setCrm(request.getCrm());
        medico.setEspecialidade(especialidade);

        medicoRepository.save(medico);
    }
}