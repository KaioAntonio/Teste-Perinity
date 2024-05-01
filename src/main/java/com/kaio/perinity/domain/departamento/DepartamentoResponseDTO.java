package com.kaio.perinity.domain.departamento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartamentoResponseDTO {

    private String nomeDepartamento;
    private Long qtdPessoas;
    private Long qtdTarefas;
}
