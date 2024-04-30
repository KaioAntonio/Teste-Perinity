package com.kaio.perinity.domain.tarefa;

import com.kaio.perinity.domain.departamento.Departamento;
import lombok.Data;

@Data
public class TarefaResponseDTO extends TarefaRequestDTO{
    private Integer idTarefa;
    private Departamento departamento;
    private Boolean tarefaFinalizada;

}
