package com.kaio.perinity.domain.tarefa;

import com.kaio.perinity.domain.pessoa.Pessoa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {

    List<Tarefa> findByPessoa(Pessoa pessoa);

    @Query("SELECT avg(t.duracao) as media_horas " +
            "FROM Tarefa t " +
            "JOIN Pessoa p ON t.pessoa.idPessoa = p.idPessoa " +
            "WHERE p.nome LIKE :nome AND t.prazo <= :prazo " +
            "GROUP BY p.nome")
    Integer valorMedioHoras(@Param("nome") String nome,
                            @Param("prazo") LocalDate prazo);

    @Query("SELECT t " +
            "FROM Tarefa t " +
            "WHERE t.pessoa.idPessoa IS NULL " +
            "ORDER BY t.prazo ASC ")
    List<Tarefa> listarTarefaSemPessoaAlocadaComPrazosAntigos(Pageable pageable);
}
