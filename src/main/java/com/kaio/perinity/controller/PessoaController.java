package com.kaio.perinity.controller;

import com.kaio.perinity.config.exception.RegraDeNegocioException;
import com.kaio.perinity.domain.pessoa.PessoaMediaHorasDTO;
import com.kaio.perinity.domain.pessoa.PessoaNomeDepartamentoHorasDTO;
import com.kaio.perinity.domain.pessoa.PessoaRequestDTO;
import com.kaio.perinity.domain.pessoa.PessoaResponseDTO;
import com.kaio.perinity.domain.pessoa.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@RequestMapping("/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;

    @Operation(summary = "Criar uma pessoa", description = "Cria uma pessoa")
    @PostMapping
    public ResponseEntity<PessoaResponseDTO> criarPessoa(@RequestBody @Valid PessoaRequestDTO pessoa){
        return new ResponseEntity<>(pessoaService.criarPessoa(pessoa), HttpStatus.CREATED);
    }

    @Operation(summary = "Alterar uma pessoa", description = "Alterar uma pessoa")
    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> alterarPessoa(
            @PathVariable("id") Integer idPessoa,
            @RequestBody @Valid PessoaRequestDTO pessoa) throws RegraDeNegocioException {
        return new ResponseEntity<>(pessoaService.alterarPessoa(idPessoa,pessoa), HttpStatus.OK);
    }

    @Operation(summary = "Remover uma pessoa", description = "Remover uma pessoa")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPessoa(
            @PathVariable("id") Integer idPessoa) throws RegraDeNegocioException {
        pessoaService.removerPessoa(idPessoa);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar pessoas", description =
    "Listar pessoas trazendo nome, departamento, total horas gastas nas tarefas")
    @GetMapping
    public ResponseEntity<List<PessoaNomeDepartamentoHorasDTO>> listarPessoas(){
        return new ResponseEntity<>(pessoaService.listarPessoas(), HttpStatus.OK);
    }

    @Operation(summary = "Media de horas gastas", description = "Media de horas gastas por tarefa")
    @GetMapping("/gastos")
    public ResponseEntity<PessoaMediaHorasDTO> mediaHoras(
            @RequestParam String nome,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate prazo
            ){
        return new ResponseEntity<>(pessoaService.listarMediaHoraGasta(nome, prazo), HttpStatus.OK);
    }

}
