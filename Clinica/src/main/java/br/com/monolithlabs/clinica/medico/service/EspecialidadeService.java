package br.com.monolithlabs.clinica.medico.service;

import br.com.monolithlabs.clinica.medico.dto.request.AtualizarEspecialidadeRequest;
import br.com.monolithlabs.clinica.medico.dto.request.CadastroEspecialidadeRequest;
import br.com.monolithlabs.clinica.medico.entity.Especialidade;
import br.com.monolithlabs.clinica.medico.exception.EspecialidadeNaoEncontradaException;
import br.com.monolithlabs.clinica.medico.repository.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;

    @Transactional(readOnly = true)
    public List<Especialidade> listarTodas() {
        return especialidadeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Especialidade buscarPorId(Long id) {
        return especialidadeRepository.findById(id)
                .orElseThrow(() -> new EspecialidadeNaoEncontradaException(id));
    }

    @Transactional
    public void cadastrar(CadastroEspecialidadeRequest request) {
        Especialidade especialidade = new Especialidade();
        especialidade.setNome(request.getNome().trim());
        especialidadeRepository.save(especialidade);
    }

    @Transactional
    public void atualizar(Long id, AtualizarEspecialidadeRequest request) {
        Especialidade especialidade = buscarPorId(id);
        especialidade.setNome(request.getNome().trim());
        especialidadeRepository.save(especialidade);
    }
}