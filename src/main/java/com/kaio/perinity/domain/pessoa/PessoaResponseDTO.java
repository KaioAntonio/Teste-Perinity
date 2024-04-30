package com.kaio.perinity.domain.pessoa;

import com.kaio.perinity.domain.departamento.Departamento;
import lombok.Data;

@Data
public class PessoaResponseDTO extends PessoaRequestDTO{

    private Integer idPessoa;
    private Departamento departamento;
}
