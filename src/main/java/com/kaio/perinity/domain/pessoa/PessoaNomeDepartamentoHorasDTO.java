package com.kaio.perinity.domain.pessoa;

import com.kaio.perinity.domain.departamento.Departamento;
import lombok.Data;

@Data
public class PessoaNomeDepartamentoHorasDTO {

    private String nome;
    private Departamento departamento;
    private Integer duracao;

}
