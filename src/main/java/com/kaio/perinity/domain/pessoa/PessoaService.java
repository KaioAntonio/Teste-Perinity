package com.kaio.perinity.domain.pessoa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaio.perinity.config.exception.RegraDeNegocioException;
import com.kaio.perinity.domain.departamento.Departamento;
import com.kaio.perinity.domain.departamento.DepartamentoRepository;
import com.kaio.perinity.domain.tarefa.Tarefa;
import com.kaio.perinity.domain.tarefa.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final DepartamentoRepository departamentoRepository;
    private final TarefaRepository tarefaRepository;
    private final ObjectMapper objectMapper;

    public PessoaResponseDTO criarPessoa(PessoaRequestDTO pessoaRequestDTO) {
        Pessoa pessoa = objectMapper.convertValue(pessoaRequestDTO, Pessoa.class);
        Departamento departamento = new Departamento();
        departamento.setNomeDepartamento(pessoaRequestDTO.getNomeDepartamento());
        departamentoRepository.save(departamento);
        pessoa.setDepartamento(departamento);
        return objectMapper.convertValue(pessoaRepository.save(pessoa), PessoaResponseDTO.class);
    }

    public Pessoa buscarPorId(Integer id) throws RegraDeNegocioException {
        return objectMapper.convertValue(pessoaRepository.findById(id).orElseThrow(
                () -> new RegraDeNegocioException("Pessoa não foi encontrada!")), Pessoa.class);
    }

    public PessoaResponseDTO alterarPessoa(Integer idPessoa, PessoaRequestDTO pessoaAtualizacao)
            throws RegraDeNegocioException {
        Pessoa pessoa = buscarPorId(idPessoa);
        Departamento departamento = departamentoRepository.findByNomeDepartamento(pessoaAtualizacao.getNomeDepartamento());

        pessoa.setNome(pessoaAtualizacao.getNome());
        pessoa.setDepartamento(departamento);

        pessoaRepository.save(pessoa);
        return objectMapper.convertValue(pessoa, PessoaResponseDTO.class);
    }

    public void removerPessoa(Integer id) throws RegraDeNegocioException {
        Pessoa pessoa = buscarPorId(id);
        pessoaRepository.delete(pessoa);
    }

    public List<PessoaNomeDepartamentoHorasDTO> listarPessoas() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        List<PessoaNomeDepartamentoHorasDTO> response = new ArrayList<>();
        for (Pessoa pessoa : pessoas) {
            PessoaNomeDepartamentoHorasDTO pessoaNomeDepartamentoHorasDTO = new PessoaNomeDepartamentoHorasDTO();
            pessoaNomeDepartamentoHorasDTO.setNome(pessoa.getNome());
            pessoaNomeDepartamentoHorasDTO.setDepartamento(pessoa.getDepartamento());
            Tarefa tarefa = tarefaRepository.findByPessoa(pessoa);
            pessoaNomeDepartamentoHorasDTO.setDuracao(tarefa.getDuracao());
            response.add(pessoaNomeDepartamentoHorasDTO);
        }
        return response;
    }

    public PessoaMediaHorasDTO listarMediaHoraGasta(String nome, LocalDate prazo) {
        Integer media = tarefaRepository.valorMedioHoras(nome, prazo);
        PessoaMediaHorasDTO pessoaMediaHorasDTO = new PessoaMediaHorasDTO();
        pessoaMediaHorasDTO.setMediaHorasGastas(media);
        return pessoaMediaHorasDTO;
    }

}
