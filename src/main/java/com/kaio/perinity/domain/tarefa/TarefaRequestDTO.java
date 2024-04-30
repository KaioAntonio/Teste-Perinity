package com.kaio.perinity.domain.tarefa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TarefaRequestDTO {

    private String titulo;
    private String descricao;
    private LocalDate prazo;
    private Integer duracao;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String nomeDepartamento;
}
