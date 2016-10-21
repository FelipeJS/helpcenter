package helpcenter.web.rest;

import helpcenter.HelpcenterApp;

import helpcenter.domain.Chamado;
import helpcenter.domain.User;
import helpcenter.repository.ChamadoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import helpcenter.domain.enumeration.Status;
import helpcenter.domain.enumeration.Empresa;
import helpcenter.domain.enumeration.Problema;
import helpcenter.domain.enumeration.Severidade;
/**
 * Test class for the ChamadoResource REST controller.
 *
 * @see ChamadoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HelpcenterApp.class)
public class ChamadoResourceIntTest {

    private static final Status DEFAULT_STATUS = Status.Aberto;
    private static final Status UPDATED_STATUS = Status.Analisando;

    private static final Empresa DEFAULT_EMPRESA = Empresa.AGOPA;
    private static final Empresa UPDATED_EMPRESA = Empresa.FIALGO;

    private static final Problema DEFAULT_PROBLEMA = Problema.Suporte;
    private static final Problema UPDATED_PROBLEMA = Problema.Programas;

    private static final String DEFAULT_DESCRICAO = "AAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBB";

    private static final Severidade DEFAULT_SEVERIDADE = Severidade.Urgente;
    private static final Severidade UPDATED_SEVERIDADE = Severidade.Alta;

    private static final String DEFAULT_SUGESTAO = "AAAAA";
    private static final String UPDATED_SUGESTAO = "BBBBB";

    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final String DEFAULT_SOLUCAO = "AAAAA";
    private static final String UPDATED_SOLUCAO = "BBBBB";

    private static final byte[] DEFAULT_ANEXO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ANEXO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_ANEXO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ANEXO_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_DATA_DE_ABERTURA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATA_DE_ABERTURA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATA_DE_ABERTURA_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_DATA_DE_ABERTURA);

    private static final ZonedDateTime DEFAULT_DATA_DE_FECHAMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATA_DE_FECHAMENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATA_DE_FECHAMENTO_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_DATA_DE_FECHAMENTO);

    @Inject
    private ChamadoRepository chamadoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChamadoMockMvc;

    private Chamado chamado;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChamadoResource chamadoResource = new ChamadoResource();
        ReflectionTestUtils.setField(chamadoResource, "chamadoRepository", chamadoRepository);
        this.restChamadoMockMvc = MockMvcBuilders.standaloneSetup(chamadoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chamado createEntity(EntityManager em) {
        Chamado chamado = new Chamado()
                .status(DEFAULT_STATUS)
                .empresa(DEFAULT_EMPRESA)
                .problema(DEFAULT_PROBLEMA)
                .descricao(DEFAULT_DESCRICAO)
                .severidade(DEFAULT_SEVERIDADE)
                .sugestao(DEFAULT_SUGESTAO)
                .email(DEFAULT_EMAIL)
                .solucao(DEFAULT_SOLUCAO)
                .anexo(DEFAULT_ANEXO)
                .anexoContentType(DEFAULT_ANEXO_CONTENT_TYPE)
                .dataDeAbertura(DEFAULT_DATA_DE_ABERTURA)
                .dataDeFechamento(DEFAULT_DATA_DE_FECHAMENTO);
        // Add required entity
        User solicitante = UserResourceIntTest.createEntity(em);
        em.persist(solicitante);
        em.flush();
        chamado.setSolicitante(solicitante);
        return chamado;
    }

    @Before
    public void initTest() {
        chamado = createEntity(em);
    }

    @Test
    @Transactional
    public void createChamado() throws Exception {
        int databaseSizeBeforeCreate = chamadoRepository.findAll().size();

        // Create the Chamado

        restChamadoMockMvc.perform(post("/api/chamados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chamado)))
                .andExpect(status().isCreated());

        // Validate the Chamado in the database
        List<Chamado> chamados = chamadoRepository.findAll();
        assertThat(chamados).hasSize(databaseSizeBeforeCreate + 1);
        Chamado testChamado = chamados.get(chamados.size() - 1);
        assertThat(testChamado.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testChamado.getEmpresa()).isEqualTo(DEFAULT_EMPRESA);
        assertThat(testChamado.getProblema()).isEqualTo(DEFAULT_PROBLEMA);
        assertThat(testChamado.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testChamado.getSeveridade()).isEqualTo(DEFAULT_SEVERIDADE);
        assertThat(testChamado.getSugestao()).isEqualTo(DEFAULT_SUGESTAO);
        assertThat(testChamado.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testChamado.getSolucao()).isEqualTo(DEFAULT_SOLUCAO);
        assertThat(testChamado.getAnexo()).isEqualTo(DEFAULT_ANEXO);
        assertThat(testChamado.getAnexoContentType()).isEqualTo(DEFAULT_ANEXO_CONTENT_TYPE);
        assertThat(testChamado.getDataDeAbertura()).isEqualTo(DEFAULT_DATA_DE_ABERTURA);
        assertThat(testChamado.getDataDeFechamento()).isEqualTo(DEFAULT_DATA_DE_FECHAMENTO);
    }

    @Test
    @Transactional
    public void checkEmpresaIsRequired() throws Exception {
        int databaseSizeBeforeTest = chamadoRepository.findAll().size();
        // set the field null
        chamado.setEmpresa(null);

        // Create the Chamado, which fails.

        restChamadoMockMvc.perform(post("/api/chamados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chamado)))
                .andExpect(status().isBadRequest());

        List<Chamado> chamados = chamadoRepository.findAll();
        assertThat(chamados).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProblemaIsRequired() throws Exception {
        int databaseSizeBeforeTest = chamadoRepository.findAll().size();
        // set the field null
        chamado.setProblema(null);

        // Create the Chamado, which fails.

        restChamadoMockMvc.perform(post("/api/chamados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chamado)))
                .andExpect(status().isBadRequest());

        List<Chamado> chamados = chamadoRepository.findAll();
        assertThat(chamados).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = chamadoRepository.findAll().size();
        // set the field null
        chamado.setDescricao(null);

        // Create the Chamado, which fails.

        restChamadoMockMvc.perform(post("/api/chamados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chamado)))
                .andExpect(status().isBadRequest());

        List<Chamado> chamados = chamadoRepository.findAll();
        assertThat(chamados).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSeveridadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chamadoRepository.findAll().size();
        // set the field null
        chamado.setSeveridade(null);

        // Create the Chamado, which fails.

        restChamadoMockMvc.perform(post("/api/chamados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chamado)))
                .andExpect(status().isBadRequest());

        List<Chamado> chamados = chamadoRepository.findAll();
        assertThat(chamados).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = chamadoRepository.findAll().size();
        // set the field null
        chamado.setEmail(null);

        // Create the Chamado, which fails.

        restChamadoMockMvc.perform(post("/api/chamados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chamado)))
                .andExpect(status().isBadRequest());

        List<Chamado> chamados = chamadoRepository.findAll();
        assertThat(chamados).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataDeAberturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = chamadoRepository.findAll().size();
        // set the field null
        chamado.setDataDeAbertura(null);

        // Create the Chamado, which fails.

        restChamadoMockMvc.perform(post("/api/chamados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chamado)))
                .andExpect(status().isBadRequest());

        List<Chamado> chamados = chamadoRepository.findAll();
        assertThat(chamados).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChamados() throws Exception {
        // Initialize the database
        chamadoRepository.saveAndFlush(chamado);

        // Get all the chamados
        restChamadoMockMvc.perform(get("/api/chamados?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(chamado.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].empresa").value(hasItem(DEFAULT_EMPRESA.toString())))
                .andExpect(jsonPath("$.[*].problema").value(hasItem(DEFAULT_PROBLEMA.toString())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
                .andExpect(jsonPath("$.[*].severidade").value(hasItem(DEFAULT_SEVERIDADE.toString())))
                .andExpect(jsonPath("$.[*].sugestao").value(hasItem(DEFAULT_SUGESTAO.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].solucao").value(hasItem(DEFAULT_SOLUCAO.toString())))
                .andExpect(jsonPath("$.[*].anexoContentType").value(hasItem(DEFAULT_ANEXO_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].anexo").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANEXO))))
                .andExpect(jsonPath("$.[*].dataDeAbertura").value(hasItem(DEFAULT_DATA_DE_ABERTURA_STR)))
                .andExpect(jsonPath("$.[*].dataDeFechamento").value(hasItem(DEFAULT_DATA_DE_FECHAMENTO_STR)));
    }

    @Test
    @Transactional
    public void getChamado() throws Exception {
        // Initialize the database
        chamadoRepository.saveAndFlush(chamado);

        // Get the chamado
        restChamadoMockMvc.perform(get("/api/chamados/{id}", chamado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chamado.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.empresa").value(DEFAULT_EMPRESA.toString()))
            .andExpect(jsonPath("$.problema").value(DEFAULT_PROBLEMA.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.severidade").value(DEFAULT_SEVERIDADE.toString()))
            .andExpect(jsonPath("$.sugestao").value(DEFAULT_SUGESTAO.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.solucao").value(DEFAULT_SOLUCAO.toString()))
            .andExpect(jsonPath("$.anexoContentType").value(DEFAULT_ANEXO_CONTENT_TYPE))
            .andExpect(jsonPath("$.anexo").value(Base64Utils.encodeToString(DEFAULT_ANEXO)))
            .andExpect(jsonPath("$.dataDeAbertura").value(DEFAULT_DATA_DE_ABERTURA_STR))
            .andExpect(jsonPath("$.dataDeFechamento").value(DEFAULT_DATA_DE_FECHAMENTO_STR));
    }

    @Test
    @Transactional
    public void getNonExistingChamado() throws Exception {
        // Get the chamado
        restChamadoMockMvc.perform(get("/api/chamados/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChamado() throws Exception {
        // Initialize the database
        chamadoRepository.saveAndFlush(chamado);
        int databaseSizeBeforeUpdate = chamadoRepository.findAll().size();

        // Update the chamado
        Chamado updatedChamado = chamadoRepository.findOne(chamado.getId());
        updatedChamado
                .status(UPDATED_STATUS)
                .empresa(UPDATED_EMPRESA)
                .problema(UPDATED_PROBLEMA)
                .descricao(UPDATED_DESCRICAO)
                .severidade(UPDATED_SEVERIDADE)
                .sugestao(UPDATED_SUGESTAO)
                .email(UPDATED_EMAIL)
                .solucao(UPDATED_SOLUCAO)
                .anexo(UPDATED_ANEXO)
                .anexoContentType(UPDATED_ANEXO_CONTENT_TYPE)
                .dataDeAbertura(UPDATED_DATA_DE_ABERTURA)
                .dataDeFechamento(UPDATED_DATA_DE_FECHAMENTO);

        restChamadoMockMvc.perform(put("/api/chamados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChamado)))
                .andExpect(status().isOk());

        // Validate the Chamado in the database
        List<Chamado> chamados = chamadoRepository.findAll();
        assertThat(chamados).hasSize(databaseSizeBeforeUpdate);
        Chamado testChamado = chamados.get(chamados.size() - 1);
        assertThat(testChamado.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testChamado.getEmpresa()).isEqualTo(UPDATED_EMPRESA);
        assertThat(testChamado.getProblema()).isEqualTo(UPDATED_PROBLEMA);
        assertThat(testChamado.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testChamado.getSeveridade()).isEqualTo(UPDATED_SEVERIDADE);
        assertThat(testChamado.getSugestao()).isEqualTo(UPDATED_SUGESTAO);
        assertThat(testChamado.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testChamado.getSolucao()).isEqualTo(UPDATED_SOLUCAO);
        assertThat(testChamado.getAnexo()).isEqualTo(UPDATED_ANEXO);
        assertThat(testChamado.getAnexoContentType()).isEqualTo(UPDATED_ANEXO_CONTENT_TYPE);
        assertThat(testChamado.getDataDeAbertura()).isEqualTo(UPDATED_DATA_DE_ABERTURA);
        assertThat(testChamado.getDataDeFechamento()).isEqualTo(UPDATED_DATA_DE_FECHAMENTO);
    }

    @Test
    @Transactional
    public void deleteChamado() throws Exception {
        // Initialize the database
        chamadoRepository.saveAndFlush(chamado);
        int databaseSizeBeforeDelete = chamadoRepository.findAll().size();

        // Get the chamado
        restChamadoMockMvc.perform(delete("/api/chamados/{id}", chamado.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Chamado> chamados = chamadoRepository.findAll();
        assertThat(chamados).hasSize(databaseSizeBeforeDelete - 1);
    }
}
