package com.kaio.perinity.domain.departamento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public List<DepartamentoResponseDTO> listarDepartamentos() {
        return departamentoRepository.listarDepartamentoComPessoasETarefas();
    }
}
