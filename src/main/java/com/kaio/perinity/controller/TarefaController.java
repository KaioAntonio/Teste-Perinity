package com.kaio.perinity.controller;

import com.kaio.perinity.config.exception.RegraDeNegocioException;
import com.kaio.perinity.domain.tarefa.TarefaAlocarPessoaDepartamentoDTO;
import com.kaio.perinity.domain.tarefa.TarefaRequestDTO;
import com.kaio.perinity.domain.tarefa.TarefaResponseDTO;
import com.kaio.perinity.domain.tarefa.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    @Operation(summary = "Criar uma tarefa", description = "Cria uma tarefa")
    @PostMapping
    public ResponseEntity<TarefaResponseDTO> criarTarefa(@RequestBody @Valid TarefaRequestDTO tarefaRequestDTO)
            throws RegraDeNegocioException {
        return new ResponseEntity<>(tarefaService.criarTarefa(tarefaRequestDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Alocar uma pessoa em uma tarefa", description = "Alocar uma pessoa em uma tarefa" +
            "com o mesmo departamento")
    @PutMapping("alocar/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> alocarPessoaComMesmoDepartamento(
            @PathVariable("idTarefa") Integer idTarefa,
            @RequestBody @Valid TarefaAlocarPessoaDepartamentoDTO tarefaAlocarPessoaDepartamentoDTO
            )
            throws RegraDeNegocioException {
        return new ResponseEntity<>(tarefaService.alocarPessoaComMesmoDepartamento(idTarefa,
                tarefaAlocarPessoaDepartamentoDTO), HttpStatus.OK);
    }

    @Operation(summary = "Finalizar uma tarefa", description = "Finalizar uma tarefa")
    @PutMapping("finalizarTarefa/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> finalizarTarefa(
            @PathVariable("idTarefa") Integer idTarefa
    ) throws RegraDeNegocioException {
        return new ResponseEntity<>(tarefaService.finalizarTarefa(idTarefa), HttpStatus.OK);
    }

    @Operation(summary = "Listar 3 tarefas sem pessoa e prazos mais antigos", description =
    "Listar 3 tarefas que estejam sem pessoa alocada com os prazos mais antigos")
    @GetMapping("/pendentes")
    public ResponseEntity<List<TarefaResponseDTO>> listarTarefasPendentes() {
        return new ResponseEntity<>(tarefaService.listarTarefaSemPessoaAlocadaComPrazosAntigos(), HttpStatus.OK);
    }

}
