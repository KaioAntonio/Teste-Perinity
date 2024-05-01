package com.kaio.perinity.domain.departamento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaio.perinity.domain.pessoa.Pessoa;
import com.kaio.perinity.domain.tarefa.Tarefa;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "DEPARTAMENTOS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DEPARTAMENTO")
    private Integer idDepartamento;

    @Column(name = "NOME_DEPARTAMENTO")
    private String nomeDepartamento;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "departamento")
    private Set<Tarefa> tarefas;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "departamento")
    private Set<Pessoa> pessoas;

}
