package com.kaio.perinity.domain.tarefa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaio.perinity.config.exception.RegraDeNegocioException;
import com.kaio.perinity.domain.departamento.Departamento;
import com.kaio.perinity.domain.departamento.DepartamentoRepository;
import com.kaio.perinity.domain.pessoa.Pessoa;
import com.kaio.perinity.domain.pessoa.PessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final PessoaService pessoaService;
    private final DepartamentoRepository departamentoRepository;
    private final ObjectMapper objectMapper;

    public TarefaResponseDTO criarTarefa(TarefaRequestDTO tarefaRequestDTO) throws RegraDeNegocioException {
        Tarefa tarefa = objectMapper.convertValue(tarefaRequestDTO, Tarefa.class);
        tarefa.setTarefaFinalizada(false);
        tarefa.setDepartamento(departamentoRepository.findByNomeDepartamento(tarefaRequestDTO.getNomeDepartamento()));
        return objectMapper.convertValue(tarefaRepository.save(tarefa), TarefaResponseDTO.class);
    }

    public Tarefa buscarPorId(Integer id) throws RegraDeNegocioException {
        return objectMapper.convertValue(tarefaRepository.findById(id).orElseThrow(
                () -> new RegraDeNegocioException("Tarefa não foi encontrada!")), Tarefa.class);
    }


    public TarefaResponseDTO alocarPessoaComMesmoDepartamento(Integer idTarefa, TarefaAlocarPessoaDepartamentoDTO
                                                              tarefaAlocarPessoaDepartamentoDTO)
            throws RegraDeNegocioException {
        Tarefa tarefa = buscarPorId(idTarefa);
        Pessoa pessoa = pessoaService.buscarPorId(tarefaAlocarPessoaDepartamentoDTO.getIdPessoa());
        Departamento departamento = departamentoRepository.findByNomeDepartamento(
                tarefa.getDepartamento().getNomeDepartamento());
        if(!Objects.equals(pessoa.getDepartamento().getIdDepartamento(), departamento.getIdDepartamento())){
          throw new RegraDeNegocioException("Departamentos diferentes, pessoa não alocada!");
        }
        tarefa.setPessoa(pessoa);
        return objectMapper.convertValue(tarefaRepository.save(tarefa), TarefaResponseDTO.class);
    }

    public TarefaResponseDTO finalizarTarefa(Integer idTarefa) throws RegraDeNegocioException {
        Tarefa tarefa = buscarPorId(idTarefa);
        tarefa.setTarefaFinalizada(true);
        return objectMapper.convertValue(tarefaRepository.save(tarefa), TarefaResponseDTO.class);
    }


}
