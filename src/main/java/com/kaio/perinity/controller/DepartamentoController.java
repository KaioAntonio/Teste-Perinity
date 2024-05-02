package com.kaio.perinity.controller;

import com.kaio.perinity.domain.departamento.DepartamentoResponseDTO;
import com.kaio.perinity.domain.departamento.DepartamentoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("/departamentos")
@RequiredArgsConstructor
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @Operation(summary = "Listar departamentos", description =
            "Listar departamento e quantidade de pessoas e tarefas")
    @GetMapping
    public ResponseEntity<List<DepartamentoResponseDTO>> listarPessoas(){
        return new ResponseEntity<>(departamentoService.listarDepartamentos(), HttpStatus.OK);
    }
}
