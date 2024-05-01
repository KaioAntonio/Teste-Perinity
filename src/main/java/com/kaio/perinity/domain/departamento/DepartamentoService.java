package com.kaio.perinity.domain.departamento;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public List<DepartamentoResponseDTO> listarDepartamentos() {
        return departamentoRepository.listarDepartamentoComPessoasETarefas();
    }
}
