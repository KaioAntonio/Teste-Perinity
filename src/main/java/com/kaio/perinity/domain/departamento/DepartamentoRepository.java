package com.kaio.perinity.domain.departamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {

    Departamento findByNomeDepartamento(String nomeDepartamento);

    @Query("SELECT new com.kaio.perinity.domain.departamento.DepartamentoResponseDTO(d.nomeDepartamento, " +
            "count(distinct p), " +
            "count(t)) " +
            "FROM Departamento d " +
            "left join d.pessoas p " +
            "left join p.tarefas t " +
            "GROUP BY d.nomeDepartamento "
    )
    List<DepartamentoResponseDTO> listarDepartamentoComPessoasETarefas();
}
