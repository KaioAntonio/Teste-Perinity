package com.kaio.perinity.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kaio.perinity.domain.departamento.DepartamentoRepository;
import com.kaio.perinity.domain.pessoa.PessoaRepository;
import com.kaio.perinity.domain.pessoa.PessoaService;
import com.kaio.perinity.domain.tarefa.TarefaRepository;
import com.kaio.perinity.domain.tarefa.TarefaService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class TarefaServiceTest {

    @InjectMocks
    private TarefaService tarefaService;

    @Mock
    private PessoaService pessoaService;
    @Mock
    private DepartamentoRepository departamentoRepository;
    @Mock
    private TarefaRepository tarefaRepository;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(pessoaService, "objectMapper", objectMapper);
    }



}
