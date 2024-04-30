package com.kaio.perinity.domain.pessoa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PessoaRequestDTO {

    private String nome;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String nomeDepartamento;
}
