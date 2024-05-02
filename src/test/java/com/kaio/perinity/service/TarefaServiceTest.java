package com.kaio.perinity.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kaio.perinity.config.exception.RegraDeNegocioException;
import com.kaio.perinity.domain.departamento.Departamento;
import com.kaio.perinity.domain.departamento.DepartamentoRepository;
import com.kaio.perinity.domain.pessoa.Pessoa;
import com.kaio.perinity.domain.pessoa.PessoaService;
import com.kaio.perinity.domain.tarefa.Tarefa;
import com.kaio.perinity.domain.tarefa.TarefaAlocarPessoaDepartamentoDTO;
import com.kaio.perinity.domain.tarefa.TarefaRepository;
import com.kaio.perinity.domain.tarefa.TarefaRequestDTO;
import com.kaio.perinity.domain.tarefa.TarefaResponseDTO;
import com.kaio.perinity.domain.tarefa.TarefaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
        ReflectionTestUtils.setField(tarefaService, "objectMapper", objectMapper);
    }

    @Test
    public void quandoTentarCriarTarefaRetornarSucesso() throws RegraDeNegocioException {
        Tarefa tarefa = mockTarefa();
        Departamento departamento = mockDepartamento();
        TarefaRequestDTO tarefaRequestDTO = mockTarefaRequestDTO();

        when(departamentoRepository.findByNomeDepartamento(anyString())).thenReturn(departamento);
        when(tarefaRepository.save(any())).thenReturn(tarefa);

        TarefaResponseDTO response = tarefaService.criarTarefa(tarefaRequestDTO);

        assertNotNull(response);
        assertEquals(tarefaRequestDTO.getTitulo(), response.getTitulo());
        assertEquals(tarefaRequestDTO.getNomeDepartamento(), response.getDepartamento().getNomeDepartamento());
    }

    @Test
    public void quandoTentarCriarTarefaComDepartamentoInvalidoRetornarExcecao() {
        TarefaRequestDTO tarefaRequestDTO = mockTarefaRequestDTO();
        when(departamentoRepository.findByNomeDepartamento(anyString())).thenReturn(null);

        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
                () -> tarefaService.criarTarefa(tarefaRequestDTO));

        assertEquals(exception.getMessage(), "Departamento não existente, favor inserir um válido!");
    }

    @Test
    public void quandoTentarBuscarTarefaPorIdRetornarSucesso() throws RegraDeNegocioException {
        Tarefa tarefa = mockTarefa();
        when(tarefaRepository.findById(anyInt())).thenReturn(Optional.of(tarefa));

        Tarefa response = tarefaService.buscarPorId(anyInt());

        assertNotNull(response);
    }

    @Test
    public void quandoTentarAlocarPessoaComMesmoDepartamentoRetornarSucesso() throws RegraDeNegocioException {
        Tarefa tarefa = mockTarefa();
        Pessoa pessoa = mockPessoa();
        TarefaAlocarPessoaDepartamentoDTO tarefaAlocarPessoaDepartamentoDTO =  mockTarefaAlocarPessoa();
                                                        
        Departamento departamento = mockDepartamento();

        when(tarefaRepository.findById(anyInt())).thenReturn(Optional.of(tarefa));
        when(pessoaService.buscarPorId(anyInt())).thenReturn(pessoa);
        when(departamentoRepository.findByNomeDepartamento(anyString())).thenReturn(departamento);
        when(tarefaRepository.save(any())).thenReturn(tarefa);

        TarefaResponseDTO response = tarefaService.alocarPessoaComMesmoDepartamento
                (1, tarefaAlocarPessoaDepartamentoDTO);

        assertNotNull(response);
    }

    @Test
    public void quandoTentarAlocarPessoaComMesmoDepartamentoRetornarExcecao() throws RegraDeNegocioException {
        Tarefa tarefa = mockTarefa();
        Pessoa pessoa = mockPessoa();
        TarefaAlocarPessoaDepartamentoDTO tarefaAlocarPessoaDepartamentoDTO =  mockTarefaAlocarPessoa();

        Departamento departamento = mockDepartamento();
        departamento.setIdDepartamento(2);

        when(tarefaRepository.findById(anyInt())).thenReturn(Optional.of(tarefa));
        when(pessoaService.buscarPorId(anyInt())).thenReturn(pessoa);
        when(departamentoRepository.findByNomeDepartamento(anyString())).thenReturn(departamento);

        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
                () -> tarefaService.alocarPessoaComMesmoDepartamento(1, tarefaAlocarPessoaDepartamentoDTO));

        assertEquals(exception.getMessage(), "Departamentos diferentes, pessoa não alocada!");
    }

    @Test
    public void quandoTentarFinalizarTarefaRetornarSucesso() throws RegraDeNegocioException {
        Tarefa tarefa = mockTarefa();

        when(tarefaRepository.findById(anyInt())).thenReturn(Optional.of(tarefa));
        tarefa.setTarefaFinalizada(true);
        when(tarefaRepository.save(any())).thenReturn(tarefa);

        TarefaResponseDTO responseDTO = tarefaService.finalizarTarefa(1);

        assertNotNull(responseDTO);
        assertEquals(true, responseDTO.getTarefaFinalizada());
    }

    @Test
    public void quandoTentarListarTarefaSemPessoaAlocadoRetornarSucesso() {
        List<TarefaResponseDTO> responseDTOS = tarefaService.listarTarefaSemPessoaAlocadaComPrazosAntigos();

        assertNotNull(responseDTOS);
    }

    private static TarefaAlocarPessoaDepartamentoDTO mockTarefaAlocarPessoa() {
        TarefaAlocarPessoaDepartamentoDTO tarefaAlocarPessoaDepartamentoDTO = new TarefaAlocarPessoaDepartamentoDTO();
        tarefaAlocarPessoaDepartamentoDTO.setIdPessoa(1);
        return tarefaAlocarPessoaDepartamentoDTO;
    }

    @Test
    public void quandoTentarBuscarTarefaPorIdRetornarExcecao() {
        when(tarefaRepository.findById(anyInt())).thenReturn(Optional.empty());

        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
                () -> tarefaService.buscarPorId(anyInt()));

        assertEquals(exception.getMessage(), "Tarefa não foi encontrada!");
    }

    private static TarefaRequestDTO mockTarefaRequestDTO() {
        TarefaRequestDTO tarefaRequestDTO = new TarefaRequestDTO();
        tarefaRequestDTO.setTitulo("Montagem de PC");
        tarefaRequestDTO.setPrazo(LocalDate.now());
        tarefaRequestDTO.setNomeDepartamento("TI");
        tarefaRequestDTO.setDuracao(10);
        tarefaRequestDTO.setDescricao("Montar o pc do cliente.");
        return tarefaRequestDTO;
    }

    private static Pessoa mockPessoa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setIdPessoa(1);
        pessoa.setNome("Kaio");
        pessoa.setDepartamento(mockDepartamento());
        return pessoa;
    }

    private static Departamento mockDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setNomeDepartamento("TI");
        departamento.setIdDepartamento(1);
        return departamento;
    }

    private static Tarefa mockTarefa() {
        Tarefa tarefa = new Tarefa();
        tarefa.setIdTarefa(1);
        tarefa.setTarefaFinalizada(false);
        tarefa.setPessoa(mockPessoa());
        tarefa.setDepartamento(mockDepartamento());
        tarefa.setPrazo(LocalDate.now());
        tarefa.setDuracao(10);
        tarefa.setTitulo("Montagem de PC");
        return tarefa;
    }

}
