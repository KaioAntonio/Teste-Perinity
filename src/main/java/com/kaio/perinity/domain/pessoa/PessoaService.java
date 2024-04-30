package com.kaio.perinity.domain.pessoa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaio.perinity.config.exception.RegraDeNegocioException;
import com.kaio.perinity.domain.departamento.Departamento;
import com.kaio.perinity.domain.departamento.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final DepartamentoRepository departamentoRepository;
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

}
