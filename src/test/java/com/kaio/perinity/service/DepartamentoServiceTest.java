package com.kaio.perinity.service;

import com.kaio.perinity.domain.departamento.DepartamentoRepository;
import com.kaio.perinity.domain.departamento.DepartamentoResponseDTO;
import com.kaio.perinity.domain.departamento.DepartamentoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DepartamentoServiceTest {

    @InjectMocks
    private DepartamentoService departamentoService;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Test
    public void quandoTentarListarDepartamentosRetornarSucesso() {
        DepartamentoResponseDTO departamentoResponseDTO = getDepartamentoResponseDTO();
        List<DepartamentoResponseDTO> departamentoResponseDTOList = new ArrayList<>();
        departamentoResponseDTOList.add(departamentoResponseDTO);
        when(departamentoRepository.listarDepartamentoComPessoasETarefas()).thenReturn(departamentoResponseDTOList);

        List<DepartamentoResponseDTO> response = departamentoService.listarDepartamentos();

        assertNotNull(response);
        assertEquals(departamentoResponseDTO.getNomeDepartamento(), response.get(0).getNomeDepartamento());
        assertEquals(departamentoResponseDTO.getQtdTarefas(), response.get(0).getQtdTarefas());
        assertEquals(departamentoResponseDTO.getQtdPessoas(), response.get(0).getQtdPessoas());
    }

    private static DepartamentoResponseDTO getDepartamentoResponseDTO() {
        DepartamentoResponseDTO departamentoResponseDTO = new DepartamentoResponseDTO();
        departamentoResponseDTO.setNomeDepartamento("TI");
        departamentoResponseDTO.setQtdTarefas(1L);
        departamentoResponseDTO.setQtdPessoas(2L);
        return departamentoResponseDTO;
    }

}
