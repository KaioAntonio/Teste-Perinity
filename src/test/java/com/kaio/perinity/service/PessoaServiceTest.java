package com.kaio.perinity.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kaio.perinity.config.exception.RegraDeNegocioException;
import com.kaio.perinity.domain.departamento.Departamento;
import com.kaio.perinity.domain.departamento.DepartamentoRepository;
import com.kaio.perinity.domain.pessoa.Pessoa;
import com.kaio.perinity.domain.pessoa.PessoaMediaHorasDTO;
import com.kaio.perinity.domain.pessoa.PessoaNomeDepartamentoHorasDTO;
import com.kaio.perinity.domain.pessoa.PessoaRepository;
import com.kaio.perinity.domain.pessoa.PessoaRequestDTO;
import com.kaio.perinity.domain.pessoa.PessoaResponseDTO;
import com.kaio.perinity.domain.pessoa.PessoaService;
import com.kaio.perinity.domain.tarefa.Tarefa;
import com.kaio.perinity.domain.tarefa.TarefaRepository;
import com.kaio.perinity.domain.tarefa.TarefaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PessoaServiceTest {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;
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

    @Test
    public void quandoTentarCriarPessoaRetornarSucesso() {
        Pessoa pessoa = mockPessoa();
        PessoaRequestDTO pessoaRequestDTO = mockPessoaRequestDTO();

        when(departamentoRepository.save(any())).thenReturn(mockDepartamento());
        when(pessoaRepository.save(any())).thenReturn(pessoa);

        PessoaResponseDTO pessoaResponseDTO = pessoaService.criarPessoa(pessoaRequestDTO);

        assertEquals(pessoa.getNome(), pessoaResponseDTO.getNome());
    }

    @Test
    public void quandoTentarAlterarPessoaRetornarSucesso() throws RegraDeNegocioException {
        Pessoa pessoa = mockPessoa();
        Departamento departamento = mockDepartamento();
        PessoaRequestDTO pessoaRequestDTO = mockPessoaRequestDTO();
        pessoaRequestDTO.setNome("Alex");
        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.of(pessoa));
        when(departamentoRepository.findByNomeDepartamento(any())).thenReturn(departamento);
        when(pessoaRepository.save(any())).thenReturn(pessoa);

        PessoaResponseDTO pessoaResponseDTO = pessoaService.alterarPessoa(1, pessoaRequestDTO);

        assertNotEquals(pessoa.getNome(), pessoaResponseDTO.getNome());
    }

    @Test
    public void quandoTentarRemoverPessoaRetornarSucesso() throws RegraDeNegocioException {
        Pessoa pessoa = mockPessoa();
        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.of(pessoa));

        pessoaService.removerPessoa(anyInt());

        verify(pessoaRepository, times(1)).delete(pessoa);
    }

    @Test
    public void quandoTentarListarPessoasRetornarSucesso() {
        Pessoa pessoa = mockPessoa();
        Tarefa tarefa = mockTarefa();

        List<Pessoa> pessoaList = new ArrayList<>();
        List<Tarefa> tarefaList = new ArrayList<>();
        pessoaList.add(pessoa);
        tarefaList.add(tarefa);

        when(pessoaRepository.findAll()).thenReturn(pessoaList);
        when(tarefaRepository.findByPessoa(any())).thenReturn(tarefaList);

        List<PessoaNomeDepartamentoHorasDTO> response = pessoaService.listarPessoas();

        assertNotNull(response);
        assertEquals(pessoa.getNome(), response.get(0).getNome());
        assertEquals(pessoa.getDepartamento(), response.get(0).getDepartamento());
        assertEquals(tarefa.getDuracao(), response.get(0).getDuracao());
    }

    @Test
    public void quandoTentarListarMediaHorasGastasRetornarSuscesso() {
        Integer duracaoMedia = 20;
        String nomePessoa = "Kaio";
        LocalDate prazo = LocalDate.now();

        when(tarefaRepository.valorMedioHoras(anyString(), any())).thenReturn(duracaoMedia);

        PessoaMediaHorasDTO response = pessoaService.listarMediaHoraGasta(nomePessoa, prazo);

        assertNotNull(response);
        assertEquals(duracaoMedia, response.getMediaHorasGastas());
    }

    @Test
    public void quandoTentarBuscarPorIdRetornarSucesso() throws RegraDeNegocioException {
        Pessoa pessoa = mockPessoa();
        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.of(pessoa));

        Pessoa response = pessoaService.buscarPorId(anyInt());

        assertNotNull(response);
    }

    @Test
    public void quandoTentarBuscarPorIdRetornarNaoEcontrado() {
        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.empty());

        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
                () -> pessoaService.buscarPorId(anyInt()));

        assertEquals(exception.getMessage(), "Pessoa não foi encontrada!");
    }

    private static Pessoa mockPessoa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setIdPessoa(1);
        pessoa.setNome("Kaio");
        pessoa.setDepartamento(mockDepartamento());
        return pessoa;
    }

    private static PessoaRequestDTO mockPessoaRequestDTO() {
        PessoaRequestDTO pessoaRequestDTO = new PessoaRequestDTO();
        pessoaRequestDTO.setNome("Kaio");
        pessoaRequestDTO.setNomeDepartamento("Ti");
        return pessoaRequestDTO;
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
